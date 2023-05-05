import cv2
import mediapipe as mp
import numpy as np
import threading
import tensorflow as tf

no_of_timesep_squat = 5  # TODO: was 60
label = "Squat...."
# labels arr, in each label index counts how many times apeared during training - label 6 is error
training_labels = np.array([0, 0, 0, 0, 0, 0, 0])


def make_landmark_timestep(results):
    cur_lm = []
    for id, lm in enumerate(results.pose_landmarks.landmark):
        cur_lm.append(lm.x)
        cur_lm.append(lm.y)
        cur_lm.append(lm.z)
        # cur_lm.append(lm.visibility)
    return cur_lm


def draw_landmark_on_image(mpDraw, results, img, mpPose):
    mpDraw.draw_landmarks(img, results.pose_landmarks, mpPose.POSE_CONNECTIONS)
    for id, lm in enumerate(results.pose_landmarks.landmark):
        h, w, c = img.shape
        cx, cy = int(lm.x * w), int(lm.y * h)
        # crosses inside the circles
        cv2.circle(img, (cx, cy), 1, (255, 255, 255), cv2.FILLED)
    return img


def draw_class_on_image(label, counter, img):
    font = cv2.FONT_HERSHEY_SIMPLEX
    bottomLeftCornerOfText = (10, 30)
    fontScale = 1
    fontColor = (0, 255, 0)
    thickness = 2
    lineType = 2

    cv2.putText(img, label,
                bottomLeftCornerOfText,
                font,
                fontScale,
                fontColor,
                thickness,
                lineType)

    strCounter = "Count: " + str(counter)

    bottomLeftCornerOfText = (10, 60)

    cv2.putText(img, strCounter,
                bottomLeftCornerOfText,
                font,
                fontScale,
                fontColor,
                thickness,
                lineType)
    return img


def print_success():
    major = np.argmax(training_labels)
    total = sum(training_labels)
    print(f"major training label = {major} | sum = {total}\n"
          f"success = {round(training_labels[0] * 100 / total, 1)} %")


def detect(model, lm_list):
    global label
    lm_list = np.array(lm_list)
    lm_list = np.expand_dims(lm_list, axis=-1)
    results = model.predict(lm_list, verbose=0)

    num_labels = 6

    labels = {
        0: "Normal",
        1: "Uneven back",
        2: "Feet too narrow",
        3: "Buttock too high",
        4: "Knees too wide",
        5: "Knees inward"
    }
    # print(f"results= {results}")

    label_index = np.argmax(results) % num_labels
    # print(label_index)

    if label_index < len(labels):
        label = labels[int(label_index)]
    else:
        label = 'Invalid label index'
        label_index = 6
    # print(label)

    training_labels[label_index] += 1
    return label


def is_squatting(landmarks, prev_nose, prev_r_hip, prev_l_hip):
    mpPose = mp.solutions.pose

    nose = landmarks[mpPose.PoseLandmark.NOSE]
    left_hip = landmarks[mpPose.PoseLandmark.LEFT_HIP]
    right_hip = landmarks[mpPose.PoseLandmark.RIGHT_HIP]

    # Check if the person is lowering their hips by comparing their current position to the previous position
    if nose.y + 0.02 < prev_nose.y and right_hip.y + 0.02 < prev_r_hip.y and left_hip.y + 0.02 < prev_l_hip.y:
        return True
    else:
        return False


def squatDetect():
    n_time_steps = no_of_timesep_squat

    lm_list = []

    mpPose = mp.solutions.pose
    pose = mpPose.Pose()

    mpDraw = mp.solutions.drawing_utils

    # model = tf.keras.models.load_model('../model/squat_model.h5')
    # model = tf.keras.models.load_model('../model/squat_model1.h5')
    # model = tf.keras.models.load_model('../model/squat_model2.h5')
    # model = tf.keras.models.load_model('../model/squat_model3.h5')
    # model = tf.keras.models.load_model('../model/small_squat_model1.h5')
    # model = tf.keras.models.load_model('../model/small_squat_model2.h5')
    # model = tf.keras.models.load_model('../model/small_squat_model3.h5')
    # model = tf.keras.models.load_model('../model/squat_cnn_model.h5')
    # model = tf.keras.models.load_model('../model/small_squat_cnn_model.h5')
    # model = tf.keras.models.load_model('medium_squat_cnn_model.h5')
    # model = tf.keras.models.load_model('medium_squat_cnn_model1.h5')
    # model = tf.keras.models.load_model('medium_squat_cnn_model2.h5')
    model = tf.keras.models.load_model('medium_squat_cnn_model3.h5')

    # print(model.input_shape)
    cap = cv2.VideoCapture(0)
    # cap = cv2.VideoCapture("../DataCollect/Squat_video/Resized/squat_122.mp4")
    # cap = cv2.VideoCapture("../DataCollect/Squat_video/Resized/squat_125.mp4")
    # cap = cv2.VideoCapture("../DataCollect/Squat_video/Resized/squat_115.mp4")

    state = "standing"
    # success, img = cap.read()
    # prev_results = pose.process(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
    # prev_landmarks = make_landmark_timestep(prev_results)
    # nose = prev_landmarks[mpPose.PoseLandmark.NOSE.value]
    # r_hip = prev_landmarks[mpPose.PoseLandmark.RIGHT_HIP.value]
    # l_hip = prev_landmarks[mpPose.PoseLandmark.LEFT_HIP.value]
    nose = 0
    r_hip = 0
    l_hip = 0
    flag = True
    counter = 0
    fps = 0
    while True:

        success, img = cap.read()
        if img is None:
            print_success()
            raise Exception("Image is empty")

        imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

        results = pose.process(imgRGB)
        global label

        try:
            angle = 0
            # landmarks = results.pose_landmarks.landmark
            landmarks = results.pose_landmarks

            # prevents the camera from closing if there are no landmarks
            if landmarks is not None:
                landmarks = landmarks.landmark

                if flag:
                    nose = landmarks[mpPose.PoseLandmark.NOSE.value]
                    r_hip = landmarks[mpPose.PoseLandmark.RIGHT_HIP.value]
                    l_hip = landmarks[mpPose.PoseLandmark.LEFT_HIP.value]
                    flag = False

                # print(f"nose={nose.y}\nrhip={r_hip.y}\nlhip={l_hip.y}\n")
                # Check if the user is squatting
                if is_squatting(landmarks, nose, r_hip, l_hip):
                    if state == "standing":
                        # User just started squatting
                        state = "squatting"
                else:
                    if state == "squatting":
                        # User just finished a squat
                        state = "standing"
                        counter += 1

                    nose = landmarks[mpPose.PoseLandmark.NOSE.value]
                    r_hip = landmarks[mpPose.PoseLandmark.RIGHT_HIP.value]
                    l_hip = landmarks[mpPose.PoseLandmark.LEFT_HIP.value]
                    # print("Squat count:", counter)

                cv2.putText(img, str(angle), tuple(np.multiply(
                    [landmarks[mpPose.PoseLandmark.LEFT_HIP.value].x, landmarks[mpPose.PoseLandmark.LEFT_HIP.value].y],
                    [640, 480]).astype(int)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1, cv2.LINE_AA)

                # print(angle)
                if results.pose_landmarks:
                    c_lm = make_landmark_timestep(results)

                    lm_list.append(c_lm)
                    if len(lm_list) == n_time_steps:
                        t1 = threading.Thread(target=detect, args=(model, lm_list,))
                        t1.start()
                        lm_list = []

                    img = draw_landmark_on_image(mpDraw, results, img, mpPose)
                img = draw_class_on_image(label, counter, img)
                cv2.imshow("Image", img)
                if cv2.waitKey(1) == 27:
                    break
            else:
                label = "Body not detected"

        except ValueError:
            label = "Body not detected"
            print(label)

    cap.release()
    cv2.destroyAllWindows()
    print_success()


# squatDetect()

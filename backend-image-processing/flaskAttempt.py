import flask
from flask import request, Response
import logging

# Configure the logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s [%(levelname)s] %(message)s')


from testing import squatDetect, image_processing

app = flask.Flask(__name__)
app.config['counter'] = 0
app.config['state'] = 'standing'
app.config['nose'] = None
app.config['r_hip'] = None
app.config['l_hip'] = None
app.config['flag'] = True


# to display the connection status
# @app.route('/', methods=['GET'])
# def handle_call():
#     return "Successfully Connected"
#
#
# # the get method. when we call this, it just return the text "Hey!! I'm the fact you got!!!"
# @app.route('/getfact', methods=['GET'])
# def get_fact():
#     # video = stream_video()
#     squatDetect()
#     return "Fact received!"


# @app.route('/analyze-video', methods=['POST'])
# def analyze_video(video_to_analyze):
#     video_data = request.data
#
#     if not video_data:
#         return {
#             "message": "No video data received"
#         }
#
#
#     return {
#         "message": "Video analyzed successfully",
#         "video_meta_data": video_data
#     }


@app.route('/analyze-image', methods=['POST'])
def analyze_image():
    image_data = request.data # data from client
    # other_data = request.files['other']
    #
    #
    # if not image_data:
    #     return {
    #         "message": "No image data received"
    #     }
    if 'image' not in request.files:
        return {
            "message": "No image data received"
        }
    image_file = request.files['image']
    image_data = image_file.read()
    logging.info(app.config['flag'])
    processed_image, label, counter ,state, nose, r_hip,l_hip,flag = image_processing(image_data,app.config['counter'],app.config['state'],app.config['nose'],app.config['r_hip'],app.config['l_hip'],app.config['flag'])
    app.config['counter'] = counter
    app.config['state'] = state
    app.config['nose'] = nose
    app.config['r_hip'] = r_hip
    app.config['l_hip'] = l_hip
    app.config['flag'] = flag

    # return {
    #     "message": "Image analyzed successfully",
    #     "processed_image": processed_image,
    #     "image_meta_data": {
    #         "label": label
    #     }
    # }
    logging.info(app.config['flag'])
    logging.info(app.config['state'])
    logging.info(app.config['counter'])
    return  {
        "label": label,
        "counter":app.config['counter']
    }
    # return label

    # response image as a mime type
    # return Response(processed_image, mimetype='image/jpeg')


# def stream_video():
#     url = 'https://example.com/video.mp4'  # Replace with your video stream URL
#     response = requests.get(url, stream=True)
#     return Response(response.iter_content(chunk_size=1024), mimetype='video/mp4')


# the post method. when we call this with a string containing a name, it will return the name with the text "I got your name"
# @app.route('/getname/<name>', methods=['POST'])
# def extract_name(name):
#     return "I got your name " + name


# this commands the script to run in the given port
if __name__ == '__main__':
     app.run(host="0.0.0.0", port=5002, debug=True)


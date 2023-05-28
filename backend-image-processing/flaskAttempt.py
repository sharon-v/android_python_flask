import flask
from flask import request, Response
import requests

from testing import squatDetect, image_processing

app = flask.Flask(__name__)


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
    processed_image, label = image_processing(image_data)

    # return {
    #     "message": "Image analyzed successfully",
    #     "processed_image": processed_image,
    #     "image_meta_data": {
    #         "label": label
    #     }
    # }
    return label

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


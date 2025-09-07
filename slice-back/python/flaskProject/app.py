import pyvips
import os
# img = pyvips.Image.new_from_file(r'C:\Users\ASUS\Desktop\file\file\OpenSlide\svstodzi\B20028048-1.svs',
#                                  access='sequential')
# img.dzsave(r'C:\svs\B20028048-1')

slices_path = 'C:/svs/'

from flask import Flask, Response, request

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


@app.route('/openSlide', methods=["POST"])
def openSlide():
    upload_svs = request.form['svsPath']

    img = pyvips.Image.new_from_file("" + upload_svs, access='sequential')
    store_path = slices_path + os.path.basename(upload_svs).rsplit('.', 1)[0]
    print(store_path)
    img.dzsave(store_path)

    response = Response(store_path)

    return response


if __name__ == '__main__':
    app.run()

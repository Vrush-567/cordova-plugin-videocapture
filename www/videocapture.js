var exec = require('cordova/exec');

var VideoCapturePlugin = {
    captureVideo: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, "VideoCapturePlugin", "captureVideo", []);
    }
};

module.exports = VideoCapturePlugin;

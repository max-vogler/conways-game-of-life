function Loader(urls) {
    this.onComplete = function () {
    };
    var shaders = {};
    var loader = this;

    var processShader = function (jqXHR) {
        shaders[this.key] = jqXHR.responseText;

        if (Object.keys(shaders).length == Object.keys(urls).length) {
            loader.onComplete(shaders);
        }
    };

    Object.keys(urls).forEach(function (key) {
        $.ajax({
            url: urls[key],
            dataType: 'text',
            context: {
                key: key
            },
            complete: processShader
        });
    });
}
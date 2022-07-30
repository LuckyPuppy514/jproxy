layui.define(['jquery', 'layer'], function (exports) {
    var $ = layui.jquery,
        layer = layui.layer;

    var requestUtil = {
        get: function (url, data, success) {
            $.ajax({
                url: url,
                data: data,
                type: "get",
                dataType: 'json',
                success: success,
            });
        },

        postForm: function (url, data, success) {
            $.ajax({
                url: url,
                data: data,
                type: "post",
                dataType: 'json',
                success: success,
            });
        },

        postJson: function (url, data, success) {
            $.ajax({
                url: url,
                data: data,
                contentType: "application/json; charset=utf-8",
                type: "post",
                dataType: 'json',
                success: success,
            });
        }
    };

    // var loadIndex;
    $.ajaxSetup({
        beforeSend: function () {
            // loading
            // loadIndex = layer.load(2, { time: 10 * 1000 });

            // i18n
            if (!this.url.includes('lang=')) {
                if (this.url.includes("?")) {
                    this.url = this.url + '&lang=' + localStorage.lang;
                } else {
                    this.url = this.url + '?lang=' + localStorage.lang;
                }
            }
        },

        error: (res) => {
            // console.log(JSON.stringify(res));
            layer.msg(res.statusText);
        },

        complete: function (res) {
            // layer.close(loadIndex);
            if (res.statusText == "parsererror") {
                top.location.href = '/page/login?lang=' + localStorage.lang;
            }
        }
    });

    exports('requestUtil', requestUtil);
});
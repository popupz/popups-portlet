var Popups =  {

    showPopup: function(config) {
        AUI().use('aui-dialog', function (A) {

            var dialogButtons = [
                {
                    label: config.strings.confirm,
                    handler: function () {
                        this.close();
                    }
                }
            ];

            var dialog = new A.Dialog({
                buttons: config.mustConfirm ? dialogButtons : [],
                close: !config.mustConfirm,
                width: config.width,
                modal: true,
                title: config.title,
                bodyContent: config.parseTemplate ? A.Lang.sub(config.bodyContent, config.model) : config.bodyContent,
                centered: true,
                strings: config.strings
            });


            dialog.render();
        });
    }
};

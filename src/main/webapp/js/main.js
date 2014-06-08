var Popups =  {

    showPopup: function(config) {
        AUI().use('aui-modal', function (A) {

            var dialogConfig = {
                bodyContent: config.parseTemplate ? A.Lang.sub(config.bodyContent, config.model) : config.bodyContent,
                centered: true,
                headerContent: '<h3>' + config.title + '</h3>',
                modal: config.mustConfirm,
                render: '#modal',
                width: config.width,
                zIndex: 10000
            };

            if (config.mustConfirm) {
                dialogConfig.toolbars = {};
            }

            var modal = new A.Modal(dialogConfig);

            if (config.mustConfirm) {
                modal.addToolbar(
                    [
                        {
                            label: config.strings.confirm,
                            on: {
                                click: function() {
                                    if (config.onConfirm) {
                                        config.onConfirm();
                                    }
                                    modal.hide();
                                }
                            }
                        }
                    ]
                );
            }

            modal.render();

        });;
    }
};

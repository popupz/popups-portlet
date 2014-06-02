var Popups =  {

    _showPopup_liferay_6_1: function(config) {
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
    },
    _showPopup: function(config) {

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

        });
    },

    showPopup: function(config) {
        var isLiferay61 = AUI.version === '3.4.0'; // there seems to be no easy way to get this info from liferay

        if (isLiferay61) this._showPopup_liferay_6_1(config);
        else this._showPopup(config);
    }
};

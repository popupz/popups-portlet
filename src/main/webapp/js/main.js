
AUI.add('plugin-popup', function(A) {

    function PopupDialog(config) {

        var dialogButtons = [
            {
                label: config.strings.confirm,
                handler: function () {
                    this.close();
                }
            }
        ];


        PopupDialog.superclass.constructor.call(this, {
            buttons: config.mustConfirm ? dialogButtons : [],
            close: !config.mustConfirm,
            width: config.width,
            modal: true,
            title: config.title,
            bodyContent: config.parseTemplate ? A.Lang.sub(config.bodyContent, config.model) : config.bodyContent,
            centered: true,
            strings: config.strings
        })
    }
    PopupDialog.NAME = 'dialog';

    A.extend(PopupDialog, A.Dialog);

    A.PopupDialog = PopupDialog;

}, '1.0' ,{requires:['aui-dialog', 'aui-overlay-manager', 'dd-constrain']});
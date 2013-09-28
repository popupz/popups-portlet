if (CKEDITOR) {
    CKEDITOR.config.extraPlugins = 'placeholders';

    CKEDITOR.config.toolbar_popups = [
        ['Styles', 'FontSize', '-', 'TextColor', 'BGColor'],
        ['Bold', 'Italic', 'Underline', 'StrikeThrough'],
        ['Subscript', 'Superscript'],
        '/',
        ['Undo', 'Redo', '-', 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'SelectAll', 'RemoveFormat'],
        ['Find', 'Replace', 'SpellCheck'],
        ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
        ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
        '/',
        ['Source'],
        ['Link', 'Unlink', 'Anchor'],
        ['Placeholders', 'Image', 'Flash', 'Table', '-', 'Smiley', 'SpecialChar', 'LiferayPageBreak']
    ];
}
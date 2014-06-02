CKEDITOR.plugins.add( 'placeholders',
    {
        init: function( editor )
        {
            editor.addCommand( 'placeholdersDialog' , new CKEDITOR.dialogCommand('placeholdersDialog') );
            editor.ui.addButton( 'Placeholders',
                {
                    label: 'Insert placeholder',
                    command: 'placeholdersDialog',
                    icon: this.path + 'images/icon.png'
                });
            CKEDITOR.dialog.add( 'placeholdersDialog', function (editor)
            {
                return {
                    title: 'Insert placeholder',
                    minWidth: 200,
                    minHeight: 60,
                    contents:
                        [
                            {
                                id: 'tab1',
                                label: 'placeholders',
                                elements:
                                    [
                                        {
                                            type: 'select',
                                            id: 'placeholder',
                                            label: 'Select your placeholder',

                                            items: [ [ 'User full name','{userFullName}'], [ 'User id', '{userId}' ], [ 'Group id', '{groupId}' ], [ 'CompanyId', '{companyId}' ] ],
                                            'default': '{userFullName}',
                                            required : true,
                                            commit: function( data )
                                            {
                                                data.contents = this.getValue();
                                            }
                                        }
                                    ]
                            }
                        ],
                    onOk : function()
                    {
                        var data = {};
                        this.commitContent( data );

                        editor.insertText(data.contents);
                    }
                };

            });

        }
    });
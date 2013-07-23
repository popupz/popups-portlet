AUI.add('ac-selectbox', function(A) {

    var SELECTION = 'selection',
        KEY_TAB = 9;

    function ACSelectionPlugin(config) {

        var dataSource = new A.DataSource.IO({
            source: config.dataUrl
        });

        dataSource.plug(A.Plugin.DataSourceJSONSchema, {
            schema: {
                resultListLocator: "response"
            }
        });

        ACSelectionPlugin.superclass.constructor.call(this, {
            source: dataSource,
            inputNode: config.host,
            render: true,
            resultHighlighter: 'startsWith',
            resultTextLocator: function (response) {
                return response.name;
            },
            requestTemplate: '&q={query}'
        })
    }

    A.extend(ACSelectionPlugin, A.AutoCompleteList, {

        initializer: function(config) {

            var self = this;
            var inputNode = self.get('inputNode');

            this.on('results', function (e) {
                if (!e.results.length) { // no results were matched
                    inputNode.set('value', '');
                }

                var userTypedInActualCompleteSelection = e.results.length >= 1 && e.results[0].raw.name === inputNode.get('value');

                self._updateSelectionAttribute(userTypedInActualCompleteSelection ? e.results[0].raw.key: null);
            });

            inputNode.on('blur', function(e) {
                if ((!self._mouseOverList && !self._listFocused) || self._lastInputKey === KEY_TAB) {

                    if (!self.get(SELECTION)) {
                        inputNode.set('value', '');
                    }
                }
            });

            this.on('select', function (e) {
                self._updateSelectionAttribute(e.result.raw.key);
            });
        },

        _updateSelectionAttribute: function(newVal) {
            var oldVal = this.get(SELECTION);

            if (oldVal != newVal) {
                this.set(SELECTION, newVal);
            }
        }
    }, {
        NAME      : 'autocompleteSelectionPlugin',
        NS        : 'as',
        CSS_PREFIX: A.ClassNameManager.getClassName('aclist'),
        ATTRS: {

            selection: {
                value: null
            }
        }
    });

    A.Plugin.ACSelection = ACSelectionPlugin;

}, '1.0' ,{requires:['autocomplete-list', 'node-pluginhost', 'autocomplete-highlighters', 'datasource']});



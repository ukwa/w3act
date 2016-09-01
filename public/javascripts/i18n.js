/**
 * Client side code for display language switching.
 *
 * Language selection controls should have a data-i18n-code attribute with a value of the i18n code to select.
 * Language dependent elements should have a data-message-key attribute with a value of the message identifier.
 * Language dependent elements containing HTML should have a data-message-html-key attribute with a value of the message identifier.
 */

var i18n = (function($, document, window) {
    'use strict';

    var
        config = {
            defaultI18nCode: null,
            // A map of language codes, each having a map value of messages
            languages: null,
            // JQuery selector that returns all language selector controls
            selectLanguageControlSelector: '[data-i18n-code]',
            // JQuery event to watch on elements matched by 'selectLanguageControlSelector' and respond with a language change
            selectLanguageEvent: 'change',
            // Function to cause the language selector control for the specified language to appear 'on' */
            showSelectedLanguageFn: function(i18nCode){
                // Default implementation assumes the selector is a standard button and uses the 'click' method to toggle it
                $('[data-i18n-code=\'' + i18nCode + '\']').click();
            }
        },

        selectedI18nCode = null,

        setDisplayLanguage = function(i18nCode){
            if(i18nCode !== selectedI18nCode) {
                var messages = config.languages[i18nCode];

                $('[data-message-key]').each(function(i){
                    $(this).text(messages[$(this).attr('data-message-key')]);
                });

                $('[data-message-html-key]').each(function(i){
                    $(this).html(messages[$(this).attr('data-message-html-key')]);
                });

                selectedI18nCode = i18nCode;
            }
        },

        initModule = function() {
            $(document).ready(function() {
                $(config.selectLanguageControlSelector).on(config.selectLanguageEvent, function() {
                    setDisplayLanguage($(this).attr('data-i18n-code'));
                });

                config.showSelectedLanguageFn(config.defaultI18nCode);
                setDisplayLanguage(config.defaultI18nCode);
            });
        };

    return {
        config: config,
        initModule: initModule
    };
}(jQuery, document, window));



import { initReactI18next } from 'react-i18next';
import * as en from "./locales/en/translation.json"
import * as es from "./locales/es/translation.json"
import { registerLocale } from 'react-datepicker';
import * as sp from "date-fns/locale";

const i18n = require('i18next').default;
i18n
  .use(initReactI18next) 
  .init({
    resources: {
        en: {translation: en},
        es: {translation: es}
    },
    // lng: "en",
    fallbackLng: "en",
    keySeparator: false, 

    interpolation: {
      escapeValue: false 
    }
  });
registerLocale('es', sp.es);
registerLocale('en', sp.en);
export default i18n;
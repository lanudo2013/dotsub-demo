import { FormEvent } from "react";

interface FormErrors {
    title?: string;
    filename?: string;
    creationDate?: string;
    description?: string;
}
interface FormTouches {
    title?: boolean;
    filename?: boolean;
    creationDate?: boolean;
    description?: boolean;
}
type FormValid = FormTouches;
export type FormValues = FormErrors;

export interface FormikCallback {
    errors: FormErrors;
    touched: FormTouches;
    isValid: FormValid;
    values: FormValues;
    handleChange: (event: any) => void;
    handleBlur: (event: any) => void;
    handleSubmit: (event: any) => void;
}

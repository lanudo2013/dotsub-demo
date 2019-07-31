import { WithTranslation } from "react-i18next";
import { FileFormSchema } from "./file-form-schema.interface";

export interface FileFormState extends FileFormSchema {
    file: File;
    loading: boolean;
    errorMessage: string | null;
    successMessage: string | null;
  }
export type FileFormProps = WithTranslation;

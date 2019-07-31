import { WithTranslation } from 'react-i18next';
import { FileInfo } from './file-info.interface';
export interface FilesListState {
    files: FileInfo[];
    errorMessage: string | null;
    loading: boolean;
}
export type FilesListComponentProps = WithTranslation;

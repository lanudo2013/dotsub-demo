import { WithTranslation } from 'react-i18next';
interface EntryProps {
    className?: string;
    isInvalid: boolean;
    name: string;
    onChange: (event: any, file: File | null) => void;
    filename: string;
}
export type FileInputProps = WithTranslation & EntryProps;

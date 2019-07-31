import { WithTranslation } from "react-i18next";

export interface CalendarInputState {
    value: Date;
}
interface InputProps {
    className?: string;
    name: string;
    isInvalid: boolean;
    value: Date | null;
    onSelectDate?: (value: Date) => void;
    onChange: (event: CustomEvent, value: Date) => void;
}
export type CalendarInputProps = InputProps & WithTranslation;

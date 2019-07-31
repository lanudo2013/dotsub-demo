import React, { Component, RefObject, ReactNode } from 'react';
import { withTranslation } from 'react-i18next';
import DatePicker from 'react-datepicker';
import './CalendarInput.scss';
import 'react-datepicker/dist/react-datepicker.css';
import { CalendarInputProps } from '../../classes/interfaces/calendar-input.interface';
import i18n from '../../i18n';

export class CalendarInputComponent extends Component<CalendarInputProps> {

    private inputRef: RefObject<HTMLInputElement>;

    constructor(props: CalendarInputProps) {
        super(props);
        this.inputRef = React.createRef();
        this.handleChange = this.handleChange.bind(this);
    }

    public handleChange(value: Date) {
        const event: CustomEvent = new CustomEvent<Date>('change');
        this.inputRef.current.value = value === null ? '' : value.toISOString().substring(0, 10);
        this.inputRef.current.dispatchEvent(event);
        this.props.onChange(event, value);
    }

    public render() {
        const {className, isInvalid, name, value} = this.props;
        return <div className="input-group" >
            <div className="form-control-container">
                <input type="hidden" ref={this.inputRef} name={name} id={name} />
                <DatePicker
                        selected={value}
                        onChange={this.handleChange}
                        popperClassName="picker"
                        className={'form-control ' + (className || ' ') + (isInvalid ? ' is-invalid' : ' ')}
                        calendarClassName="calendar-picker"
                        dateFormat="yyyy-MM-dd"
                        maxDate={new Date()}
                        withPortal
                        locale={i18n.language}
                        showYearDropdown
                        scrollableYearDropdown
                        yearDropdownItemNumber={15}
                        showMonthDropdown
                        isClearable
                />
            </div>
        </div>;
    }
}
export default withTranslation()(CalendarInputComponent);

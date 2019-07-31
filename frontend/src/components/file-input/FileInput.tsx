import React, { Component, ReactNode } from "react";
import { withTranslation } from "react-i18next";
import { FileInputProps } from "../../classes/interfaces/file-input.interface";

export class FileInputComponent extends Component<FileInputProps> {

    constructor(props: FileInputProps) {
        super(props);
    }

    public handleChange(event: any) {
        const file: File = event.target.files[0];
        this.props.onChange(event, file || null);
    }

    public render(): ReactNode {
        const { t, filename, name } = this.props;
        return <div className="input-group">

            <div className="custom-file">
                <input
                    type="file"
                    id="file"
                    className={'custom-file-input ' + (this.props.className || '') + (this.props.isInvalid ? ' is-invalid' : ' ')}
                    onChange={(event: any) => this.handleChange(event)}
                    name={name}
                />
                <label className={'custom-file-label ' + (this.props.className || '')} htmlFor="file">
                {filename || t('form.uploadFile.placeholder')}
                </label>
            </div>
            </div>;
    }
}
export default withTranslation()(FileInputComponent);

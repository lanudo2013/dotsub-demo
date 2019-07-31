import React, { Component, ReactNode, FormEvent, ReactElement } from "react";
import {Form, Button, Spinner} from 'react-bootstrap';
import FileInput from "../file-input/FileInput";
import { withTranslation } from "react-i18next";
import CalendarInput from "../calendar-input/CalendarInput";
import './FileForm.scss';
import { Formik } from 'formik';
import * as Yup from 'yup';
import Feedback from "../feedback/Feedback";
import { FormikCallback } from "../../classes/interfaces/formik-callback.interface";
import { FileFormState, FileFormProps } from "../../classes/interfaces/file-form.interface";
import filesService, { FilesService } from "../../services/files.service";
import { FileFormSchema } from "../../classes/interfaces/file-form-schema.interface";
import AlertMessage from "../alert-message/AlertMessage";
import { AjaxError } from "rxjs/ajax";
import { EndpointResponse } from "../../classes/interfaces/response.interface";

const MAX_FILE_SIZE: number = 15 * 1024 * 1024;

export class FileFormComponent extends Component<FileFormProps, FileFormState> {

    private model: Yup.ObjectSchema<FileFormSchema>;
    private initial: FileFormSchema;
    private fileService: FilesService = filesService;
    private formikRef: React.RefObject<Formik>;

    constructor(props: FileFormProps) {
      super(props);
      this.initial = {
        title: '',
        description: '',
        creationDate: null,
        filename: ''
      };
      this.state = {
        ...this.initial,
        loading: false,
        errorMessage: '',
        successMessage: '',
        file: null
      };
      this.model = this.createModel(props);
      this.submit = this.submit.bind(this);
      this.formikRef = React.createRef();
    }

    private createModel(props: FileFormProps): Yup.ObjectSchema<FileFormSchema> {
      const {t}  = props;
      return Yup.object().shape({
        title: Yup.string()
          .min(2, t('form.errors.minLength'))
          .max(50, t('form.errors.maxLength'))
          .matches(/^([a-zA-Z0-9 _-]+)$/, t('form.errors.alphanumeric'))
          .required(t('form.errors.required')),
        description: Yup.string()
          .min(3, t('form.errors.minLength'))
          .max(200, t('form.errors.maxLength')),
        creationDate: Yup.date()
          .required(t('form.errors.required')),
        filename: Yup.mixed().required(t('form.errors.required'))
                  .test('fileError', t('form.errors.maxFileSize'), (value: any) => {
                        return this.state.file !== null && this.state.file.size <=  MAX_FILE_SIZE;
                  })
      });

    }

    private createAlertMessage(isError: boolean, message: string): ReactElement {
        const property: string = isError ? 'errorMessage' : 'successMessage';
        const object: any = {};
        object[property] = null;
        return <AlertMessage
                isError={isError}
                message={message}
                onClose={() => this.setState(object)}
          />;
    }

    private resetForm() {
      this.setState({
        ...this.initial
      });
      this.formikRef.current.resetForm();
    }

    public submit(values: FileFormState) {
       try {
        let description: string = null;
        if (values.description) {
          description = values.description;
        }
        this.setState({
          loading: true
        });
  
        this.fileService.saveFile(values.title, description, values.creationDate.toString(),
                                 this.state.creationDate.getTimezoneOffset(), this.state.file)
        .subscribe(() => {
             this.setState({loading: false, successMessage: this.props.t('request.success')});
             this.resetForm();
        }, (error: AjaxError) => {
             const response: EndpointResponse<string> = error.response as EndpointResponse<string>;
             this.setState({loading: false, errorMessage: this.props.t('request.error.save') + '. ' +
                     (response && response.errorMessage ? (this.props.t(response.errorMessage) + ': ' + response.data + ' MB')   : '') });
        });
      } catch (e) {
        this.setState(() => { throw e; });
      }
    }

    public render(): ReactNode {
        const {t} = this.props;
        return <React.Fragment>
        {this.state.errorMessage ? this.createAlertMessage(true, this.state.errorMessage) : null}
        {this.state.successMessage ? this.createAlertMessage(false, this.state.successMessage) : null}
        <Formik 
          ref={this.formikRef}
          validationSchema={this.model}
          initialValues={this.initial}
          onSubmit={(values: FileFormState) => this.submit(values)}
          validateOnChange={true}
          validateOnBlur={true}
        >
          {({handleChange, handleBlur, handleSubmit, values, touched, isValid, errors}: FormikCallback) => {
            return <Form
                        noValidate
                        className="form-container"
                        onSubmit={(ev: FormEvent) => {
                          ev.preventDefault();
                          handleSubmit(values);
                        }}
                    >
                <Form.Group controlId="title">
                  <Form.Label className="form-label">{t('form.title')}</Form.Label>
                  <Form.Control
                                type="text"
                                className="control"
                                onChange={(ev: any) => {
                                  this.setState({
                                    title: ev.target.value
                                  });
                                  handleChange(ev);
                                  handleBlur(ev);
                                }}
                                onBlur={handleBlur}
                                value={this.state.title}
                                isInvalid={touched.title && errors.title !== undefined}
                   />
                  <Feedback isInvalid={touched.title && errors.title !== undefined} message={errors.title} />
                </Form.Group>
                <Form.Group controlId="description">
                  <Form.Label className="form-label">{t('form.description')}</Form.Label>
                  <Form.Control
                      as="textarea"
                      rows="4"
                      cols="3"
                      className="control"
                      onChange={(ev: any) => {
                        this.setState({
                          description: ev.target.value
                        });
                        handleChange(ev);
                        handleBlur(ev);
                      }}
                      onBlur={handleBlur}
                      value={this.state.description}
                      isInvalid={touched.description && errors.description !== undefined}
                  />
                  <Feedback isInvalid={touched.description && errors.description !== undefined} message={errors.description} />
                </Form.Group>
                <Form.Group controlId="creationDate">
                  <Form.Label className="form-label">{t('form.date')}</Form.Label>
                  <CalendarInput
                      className="control"
                      name="creationDate"
                      isInvalid={touched.creationDate && errors.creationDate !== undefined}
                      value={this.state.creationDate}
                      onChange={(event: Event, value: Date) => {
                          handleChange(event);
                          handleBlur(event);
                          this.setState({
                            creationDate: value
                          });
                    }}
                  />
                  <Feedback isInvalid={touched.creationDate && errors.creationDate !== undefined} message={errors.creationDate} />
                </Form.Group>
                <Form.Group controlId="filename">
                  <Form.Label className="form-label">{t('form.uploadFile')}</Form.Label>
                  <FileInput
                    className="control"
                    isInvalid={touched.filename && errors.filename !== undefined}
                    filename={this.state.filename}
                    name="filename"
                    onChange={(event: React.ChangeEvent, file: File | null) => {
                        handleChange(event);
                        handleBlur(event);
                        this.setState({
                          file,
                          filename: file ? file.name : ''
                        });
                    }}
                  />
                  <Feedback isInvalid={touched.filename && errors.filename !== undefined} message={errors.filename} />
                </Form.Group>
                <div>
                    {
                      this.state.loading ?
                          <Spinner animation="border" role="status" variant="primary">
                                      <span className="sr-only">Loading...</span>
                          </Spinner> :
                          <Button
                              variant="primary"
                              type="submit"
                              className="submit-button"
                              disabled={!isValid}
                          >
                            {t('form.submit')}
                          </Button>
                    }

                </div>
              </Form>;
          }}
        </Formik>
        </React.Fragment>;
    }
}
export default withTranslation()(FileFormComponent);

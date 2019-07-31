import React, { Component, ReactNode, ReactElement } from "react";
import { Table, Spinner } from "react-bootstrap";
import { WithTranslation, withTranslation } from "react-i18next";
import filesService, { FilesService } from "../../services/files.service";
import { FileInfo } from "../../classes/interfaces/file-info.interface";
import { Subscription } from "rxjs";
import AlertMessage from "../alert-message/AlertMessage";
import { EndpointResponse } from "../../classes/interfaces/response.interface";
import './FilesList.scss';
import { FilesListComponentProps, FilesListState } from "../../classes/interfaces/file-list.interface";

export class FilesListComponent extends Component<FilesListComponentProps, FilesListState> {
    private downloadUrl: string;
    private fservice: FilesService = filesService;
    private subscription: Subscription;

    constructor(props: FilesListComponentProps) {
        super(props);
        this.downloadUrl = this.fservice.getFilesDownloadUrl();
        this.state = {
            files: [],
            errorMessage: null,
            loading: false
        };
    }

    componentDidMount() {
        this.setState({loading: true});
        this.subscription = this.fservice.getFiles().subscribe(
            (files: FileInfo[]) => this.setState({files, loading: false}),
            (response: EndpointResponse<void>) => {
                this.setState({loading: false, errorMessage: this.props.t('request.error.getFiles') + '. ' +
                (response && response.errorMessage ? this.props.t(response.errorMessage) : '') });
            }
        );
    }

    componentWillUnmount() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
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

    private getFileUrl(id: string): string {
        return this.downloadUrl + '/' + id;
    }

    private getSize(val: number): string {
        return (val / 1024).toFixed(2);
    }

    public goToFile(id: string) {
        try {
            window.location.href = this.getFileUrl(id);
        } catch (e) {
            this.setState(() => { throw e; });
        }
    }

    public render(): ReactNode {
        const {t} = this.props;
        const fileNodes: ReactNode[] = (this.state.files || []).map((x: FileInfo) => {
            return <tr key={x.id}>
                <td><span>{x.title}</span></td>
                <td><span>{x.description}</span></td>
                <td><span>{x.creationDate}</span></td>
                <td><span>{this.getSize(x.size)}</span></td>
                <td><span  onClick={() => this.goToFile(x.id)} className="download-link">{t('filesList.header.download')}</span></td>
            </tr>;
        });
        return <div>
                {this.state.errorMessage ? this.createAlertMessage(true, this.state.errorMessage) : null}
                {
                    this.state.loading ?
                        <Spinner animation="border" role="status" variant="primary">
                            <span className="sr-only">Loading...</span>
                        </Spinner> :
                        <Table striped bordered hover >
                            <thead>
                            <tr>
                                <th>{t('filesList.header.title')}</th>
                                <th>{t('filesList.header.description')}</th>
                                <th>{t('filesList.header.creationDate')}</th>
                                <th>{t('filesList.header.size')}</th>
                                <th><span></span></th>
                            </tr>
                            </thead>
                            <tbody>
                                {fileNodes}
                            </tbody>
                        </Table>
                  }
        </div>;
    }
}
export default withTranslation()(FilesListComponent);

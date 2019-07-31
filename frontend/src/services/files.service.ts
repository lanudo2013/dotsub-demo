import { AjaxError } from 'rxjs/ajax';
import { FileInfo } from './../classes/interfaces/file-info.interface';
import { ApiPaths } from './../classes/interfaces/api-paths.interface';
import { ajax, AjaxResponse } from 'rxjs/ajax';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { EndpointResponse } from '../classes/interfaces/response.interface';

declare const BASE_URL: string;
declare const API1: any;

export class FilesService {
    private baseUrl: string;
    private api: ApiPaths;

    constructor() {
        this.baseUrl = BASE_URL;
        this.api = JSON.parse(API1) as ApiPaths;
    }

    public saveFile(title: string, description: string, creationDate: string, offset: number, file: File):
                    Observable<EndpointResponse<any>> {
        const formData: FormData = new FormData();
        formData.append('title', title);
        if (description !== null && description !== '') {
            formData.append('description', description);
        }
        formData.append('creationDate', creationDate);
        formData.append('file', file);
        formData.append('timeOffset', offset.toString());

        return ajax({
            url: this.baseUrl + this.api.POST_FILE,
            responseType: 'json',
            timeout: 60000,
            body: formData,
            method: 'POST'
        }).pipe(
            map((x: AjaxResponse) => x.response)
        );
    }

    public getFilesDownloadUrl() {
        return this.baseUrl + this.api.DOWNLOAD_FILE;
    }

    public getFiles(): Observable<FileInfo[]> {
        return ajax.get(this.baseUrl + this.api.READ_FILES).pipe(
            map((x: AjaxResponse) => x.response),
            map((x: EndpointResponse<FileInfo[]>) => x.data),
            catchError((error: AjaxError) => {
                throw error.response as EndpointResponse<void>;
            })
        );
    }
}

export default new FilesService();

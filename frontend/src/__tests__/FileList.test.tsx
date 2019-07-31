import { ReactWrapper, mount } from "enzyme";
import React from 'react'
import FilesList, { FilesListComponent } from "../components/files-list/FilesList";
import filesService from "../services/files.service";
import { FileInfo } from "../classes/interfaces/file-info.interface";
import { Table } from "react-bootstrap";
import { of } from "rxjs";

jest.mock('./../i18n', () => ({
    t: jest.fn(),
}));

jest.mock('./../services/files.service', () => ({
    saveFile: jest.fn().mockImplementation(() => of({})),
    getFiles: jest.fn().mockImplementation(() => of([])),
    getFilesDownloadUrl: jest.fn().mockReturnValue('/fake')
}));

describe('FileList component', () => {
    function createList(): FileInfo[] {
        return [
            {id: '1', description: 'A nice file', title: 'nice-today', creationDate: '2018-01-01', size: 4000},
            {id: '2', description: null, title: 'personal-password', creationDate: '2018-01-01', size: 1000},
            {id: '3', description: 'A json response to an endpoint request',
             title: 'response-json', creationDate: '2019-01-01', size: 450000},
            {id: '4', description: null, title: 'accounting', creationDate: '2019-01-01', size: 64000}
        ];
    }

    function getSize(val: number): string {
        return (val / 1024).toFixed(2);
    }

    let fileListRoot: ReactWrapper;
    let filesList: FileInfo[];

    beforeEach(() => {
        const wrapper2: ReactWrapper = mount(<FilesList />);
        fileListRoot = wrapper2.find(FilesListComponent);
        filesList = createList();
    });

    test('init call to the endpoint', () => {
       expect(filesService.getFiles).toHaveBeenCalled();
    });

    test('load files records on the table and read them', () => {
        const component: FilesListComponent = fileListRoot.instance() as FilesListComponent;
        component.setState({
            files: filesList
        });

        fileListRoot.update();
        const table: HTMLTableElement = fileListRoot.find(Table).getDOMNode();
        const rows: NodeListOf<HTMLTableRowElement> = table.querySelector('tbody').querySelectorAll('tr');
        for (let i = 0; i < rows.length; i++) {
            const row: HTMLTableRowElement = rows[i];
            const cells: NodeListOf<HTMLTableCellElement> = row.querySelectorAll('td');
            expect(cells[0].textContent).toEqual(filesList[i].title);
            expect(cells[1].textContent).toEqual(filesList[i].description || '');
            expect(cells[2].textContent).toEqual(filesList[i].creationDate);
            expect(cells[3].textContent).toEqual(getSize(filesList[i].size));
        }
     });
});

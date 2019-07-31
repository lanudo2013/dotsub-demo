import {mount, ReactWrapper} from 'enzyme';
import React from 'react';
import dateFormat from 'date-format';
import FileForm, { FileFormComponent } from '../components/file-form/FileForm';
import { CalendarInputComponent } from '../components/calendar-input/CalendarInput';
import { FileInputComponent } from '../components/file-input/FileInput';
import Feedback from '../components/feedback/Feedback';
import { FileFormState } from '../classes/interfaces/file-form.interface';
import filesService from '../services/files.service';
import { of } from 'rxjs';

jest.mock('./../i18n', () => ({
    t: jest.fn(),
}));

jest.mock('./../services/files.service', () => ({
    saveFile: jest.fn().mockImplementation(() => of({})),
    getFiles: jest.fn().mockImplementation(() => of({})),
    getFilesDownloadUrl: jest.fn().mockReturnValue('/fake')
}));

interface FeedbackError {
    position: number;
    error: string;
}

describe('FileForm component', () => {

    function getInvalidFeedback(root: ReactWrapper): FeedbackError[] {
        const elements: ReactWrapper = root.find(Feedback);
        const result: FeedbackError[] = [];
        for (let i = 0; i < elements.length; i++) {
            const domNode: HTMLDivElement = elements.at(i).getDOMNode();
            if (domNode.style.display === 'block') {
                result.push({
                    position: i,
                    error: domNode.textContent
                });
            }
        }
        return result;
    }

    let fileFormRoot: ReactWrapper;
    beforeEach(() => {
        const wrapper2: ReactWrapper = mount(<FileForm />);
        fileFormRoot = wrapper2.find(FileFormComponent);
    });

    test('setup form and bind values', () => {
        const title: string = 'FileTitle';
        const filename: string = 'myfile.txt';
        const creationDate: Date = new Date(2019, 1, 1);
        const description: string = 'This is a nice description';

        fileFormRoot.setState({
            filename,
            creationDate,
            title,
            description
        });

        const titleInput: HTMLInputElement = fileFormRoot.find('input[id="title"]').getDOMNode();
        expect(titleInput.value).toEqual(title);

        const descInput: HTMLInputElement = fileFormRoot.find('textarea[id="description"]').getDOMNode();
        expect(descInput.value).toEqual(description);

        const dateInput: HTMLInputElement = fileFormRoot.find('div.react-datepicker__input-container')
                                            .find('input[type="text"]').getDOMNode();

        expect(dateInput.value).toEqual(dateFormat.asString('yyyy-MM-dd', creationDate));
    });


    test('add valid values and click on submit. Call saveFile service method', (done) => {
        const component = fileFormRoot.instance() as FileFormComponent;
        const submitFn: Function = component.submit;
        component.submit = jest.fn().mockImplementation((x: FileFormState) => {
            submitFn(x);
        });

        const title: string = 'FileTitle';
        const filename: string = 'myfile.txt';
        const creationDate: Date = new Date(2019, 1, 1);
        const description: string = 'This is a nice description';

        fileFormRoot.setState({
            filename,
            creationDate,
            title,
            description
        });

        const titleInput: any = fileFormRoot.find('input[id="title"]');
        titleInput.simulate('change');

        const descInput: any = fileFormRoot.find('textarea[id="description"]');
        descInput.simulate('change');

        const filenameInput: ReactWrapper = fileFormRoot.find(FileInputComponent);
        const fic: FileInputComponent = filenameInput.instance() as FileInputComponent;
        const file = new File(["foo"], filename, {
            type: "text/plain",
        });
        fic.handleChange({
            target: {
                files: [file],
                value: filename,
                name: 'filename'
            }
        });

        const calendarInput: ReactWrapper = fileFormRoot.find(CalendarInputComponent);
        const cal: CalendarInputComponent =  calendarInput.instance() as CalendarInputComponent;
        cal.handleChange(creationDate);

        const submitWrapper: any = fileFormRoot.find('button[type="submit"]');
        submitWrapper.simulate('submit');
        setTimeout(() => {
            expect(component.submit).toHaveBeenCalled();
            expect(filesService.saveFile).toHaveBeenCalled();
            done();
        });

    });


    test('render feedbacks when invalid fields', (done) => {
        const title: string = '////////';
        const creationDate: Date = new Date(2019, 1, 1);
        const description: string = 'a';

        expect(getInvalidFeedback(fileFormRoot)).toEqual([]);

        fileFormRoot.setState({
            creationDate,
            description,
            title,
        });

        const titleInput: any = fileFormRoot.find('input[id="title"]');
        titleInput.simulate('change', {
            target: {
                value: title,
                name: 'title'
            }
        });

        const descInput: any = fileFormRoot.find('textarea[id="description"]');
        descInput.simulate('change', {
            target: {
                value: description,
                name: 'description'
            }
        });

        const calendarInput: ReactWrapper = fileFormRoot.find(CalendarInputComponent);
        const cal: CalendarInputComponent =  calendarInput.instance() as CalendarInputComponent;
        cal.handleChange(null);

        setTimeout(() => {
            fileFormRoot.update();
            expect(getInvalidFeedback(fileFormRoot)).toEqual([
                {position: 0, error: 'form.errors.alphanumeric'},
                {position: 1, error: 'form.errors.minLength'},
                {position: 2, error: 'form.errors.required'}
            ]);
            done();
        });
    });
});


import React, { Component, ReactNode, lazy, LazyExoticComponent, Suspense } from "react";
import "./App.scss";
import FileForm from "./file-form/FileForm";
import { withTranslation, WithTranslation } from "react-i18next";
import { ToggleButtonGroup, ToggleButton, ButtonToolbar, Spinner } from "react-bootstrap";
import FormErrorBoundary from "./form-error-boundary/FormErrorBoundary";
const FilesList: LazyExoticComponent<any> = lazy(() => import('./files-list/FilesList'));

class App extends Component<WithTranslation, {value: number}> {

    constructor(props: WithTranslation) {
        super(props);
        this.state = {
            value: 1
        };
        this.handleChange = this.handleChange.bind(this);
    }

    public handleChange(value: number) {
        this.setState({value: value as number});
    }

    render(): ReactNode {
        const { t } = this.props;
        return <div id="main" className="col-lg-6 col-xs-12 col-md-8 offset-lg-3 offset-xs-0 offset-md-2">
                <h1 className="title">{t("app.title")}</h1>
                <ButtonToolbar>
                    <ToggleButtonGroup
                                       type="radio"
                                       onChange={(value: number) => this.handleChange(value)}
                                       value={this.state.value}
                                       name="radio-g"
                    >
                        <ToggleButton value={1}>{t('option.createFile')}</ToggleButton>
                        <ToggleButton value={2}>{t('option.showFiles')}</ToggleButton>
                    </ToggleButtonGroup>
                </ButtonToolbar>
                <div className="margin-line" />
                {this.state.value === 1 && <FormErrorBoundary><FileForm /></FormErrorBoundary>}
                {this.state.value === 2 &&
                        <FormErrorBoundary>
                            <Suspense
                                fallback={
                                    <Spinner animation="border" role="status" variant="primary">
                                        <span className="sr-only">Loading...</span>
                                    </Spinner>
                                }>
                                <FilesList />
                            </Suspense>
                        </FormErrorBoundary>}
            </div>;
    }
}
export default withTranslation()(App);

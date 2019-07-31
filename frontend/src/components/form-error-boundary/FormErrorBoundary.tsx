import React, { Component, ReactNode, ErrorInfo } from "react";
import logService, { LogService } from "../../services/log.service";
import { WithTranslation, withTranslation } from "react-i18next";

interface FormErrorBoundaryState {
    hasError: boolean;
    error: Error;
}

class FormErrorBoundary extends Component<WithTranslation, FormErrorBoundaryState> {
    private logService: LogService;

    constructor(props: WithTranslation) {
        super(props);
        this.state = {
            hasError: false,
            error: null
        };
        this.logService = logService;
    }

    static getDerivedStateFromError(error: Error) {
        return { hasError: true, error };
    }

    componentDidCatch(error: Error, info: ErrorInfo) {
        this.logService.logError(error, 'high');
    }

    render(): ReactNode {
        const { t } = this.props;
        if (this.state.hasError) {
            return <div>
                <h1>{t('form.pageError')}</h1>
                <p>{ this.state.error.message }</p>
            </div>;
        }

        return this.props.children;
    }
}
export default withTranslation()(FormErrorBoundary);

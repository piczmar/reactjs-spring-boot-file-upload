import React, {Component} from 'react';
import './App.css';
import {
    ButtonGroup,
    Button,
    Col,
    Container,
    FormGroup,
    Input,
    Label,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader,
    Row
} from 'reactstrap';
import restClient from "./restClient";
import MetadataListView from "./MetadataListView";

const pageSize = 10;
const initMetaDataPaged = {
    content: [],
    totalSize: 0,
    page: 0
};

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            file: "",
            title: "",
            details: "",
            error: "",
            metaDataPaged: initMetaDataPaged,
            modalMessage: "",
            modal: false,
            blocking: false
        }
    }

    componentDidMount() {
        this.fetchMetadata(0, pageSize);
    }

    setStateAsync = (state) => {
        return new Promise((resolve) => {
            this.setState(state, resolve)
        });
    };

    toggleErrorAsync = async (error) => await this.toggleModalAsync(error);

    toggleModalAsync = async (message) => {
        await this.setStateAsync((state) => ({
            blocking: !state.blocking,
            modal: !state.modal,
            modalMessage: state.modal ? "" : message
        }));
    };

    //////////////////////// operations on test data with rest client //////////////////////////////
    fetchMetadata = async (page, sizePerPage, sortOrder) => {
        try {
            const sort = sortOrder || "asc";
            const response = await restClient.fetchAll(page, pageSize, sort);
            const metaDataPaged = {};
            metaDataPaged.content = response.content;
            metaDataPaged.totalSize = response.totalElements;
            metaDataPaged.page = page + 1;// rest api pages are 0-indexed while this component is 1-based indexed
            metaDataPaged.sizePerPage = sizePerPage;
            metaDataPaged.sortOrder = sort;
            await this.setStateAsync({metaDataPaged: metaDataPaged});
            return metaDataPaged;
        } catch (error) {
            await this.toggleErrorAsync("Cannot fetch tests");
        }
    };

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.type === 'file' ? event.target.files[0] : target.value;
        const name = target.name;
        this.setState({
            [name]: value, error: ""
        });
    };

    onUpload = async () => {
        try {
            const result = await restClient.uploadFile(this.state.file, this.state.title, this.state.details)
            if (!result) this.displayTheError('No user found');
        } catch (e) {
            await this.toggleErrorAsync(e.message);
            return;
        }
        await this.fetchMetadata(0, pageSize);
    }
    getMetadataColumns = () => {
        const keysArr = this.state.metaDataPaged.content.map(e => Object.keys(e));
        const keys = keysArr.length > 0 ? keysArr[0] : [];
        const headerParams = keys.map(k => {
            return {fieldName: k, columnName: k};
        });
        if (headerParams.length > 0) {
            // key attribute is required on one column
            headerParams[0].isKey = true;
        }
        return headerParams;
    };


    render() {
        return (
            <div className="App">
                <Container>
                    <Row>
                        <Col>
                            <h3>This is an awsome file upload app!</h3>
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <FormGroup row>
                                <Label for="file" sm={4}>File</Label>
                                <Col sm={8}>
                                    <Input type="file" name="file" id="file"
                               Input       placeholder="Select a file for upload"
                                           onChange={this.handleInputChange}
                                           valid={true}/>
                                </Col>
                            </FormGroup>
                            <FormGroup row>
                                <Label for="title" sm={4}>Title</Label>
                                <Col sm={8}>
                                    <Input type="text" name="title" id="title"
                                           placeholder="Enter title"
                                           onChange={this.handleInputChange}
                                           valid={true}/>
                                </Col>
                            </FormGroup>
                            <FormGroup row>
                                <Label for="details" sm={4}>Details</Label>
                                <Col sm={8}>
                                    <Input type="text" name="details" id="details"
                                           placeholder="Enter details"
                                           onChange={this.handleInputChange}
                                           valid={true}/>
                                </Col>
                            </FormGroup>
                        </Col>

                    </Row>
                    <Row>
                        <Col>
                            <ButtonGroup>
                                <Button
                                    disabled={this.state.title === "" || this.state.file === "" || this.state.details === ""}
                                    color="primary" size="md"
                                    onClick={this.onUpload}>Upload</Button>
                            </ButtonGroup>
                        </Col>
                    </Row>
                    <Row>
                        <hr className={"divider"}/>
                    </Row>
                    <Row>
                        <Col>
                            <MetadataListView columns={this.getMetadataColumns()}
                                              fetchData={this.fetchMetadata}
                                              data={this.state.metaDataPaged.content
                                                  ? this.state.metaDataPaged.content
                                                  : []}
                                              totalSize={this.state.metaDataPaged.totalSize}
                                              page={this.state.metaDataPaged.page}
                                              sizePerPage={this.state.metaDataPaged.sizePerPage}
                            />
                        </Col>
                    </Row>
                </Container>


                {/*Waiting modal*/}
                <Modal isOpen={this.state.blocking} toggle={() => false}
                       className={this.props.className}>
                    <ModalBody>
                        Loading...
                    </ModalBody>
                </Modal>


                {/*Error modal*/}
                <Modal isOpen={this.state.modal} className={this.props.className}>
                    <ModalHeader>{/*Error*/}</ModalHeader>
                    <ModalBody>
                        {this.state.modalMessage}
                    </ModalBody>
                    <ModalFooter>
                        <Button color="secondary" onClick={this.toggleErrorAsync}>OK</Button>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }
}

export default App;

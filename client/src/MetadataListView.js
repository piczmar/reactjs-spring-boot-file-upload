import React from 'react';
import {
    BootstrapTable,
    TableHeaderColumn
} from 'react-bootstrap-table';

export default class MetadataListView extends React.Component {
    constructor(props) {
        super(props);
        this.handlePageChange = this.handlePageChange.bind(this);
        this.handleSizePerPageChange = this.handleSizePerPageChange.bind(this);
    }

    handlePageChange(page, sizePerPage) {
        this.props.fetchData(page-1, sizePerPage); // rest api pages are 0-indexed while this component is 1-based indexed
    }

    handleSizePerPageChange(sizePerPage) {
        // When changing the size per page always navigating to the first page
        this.props.fetchData(this.props.testId,0, sizePerPage);
    }

    render() {

        const options = {
            onSortChange: this.handleSort,
            onPageChange: this.handlePageChange,
            onSizePerPageList: this.handleSizePerPageChange,
            page: this.props.page,
            hideSizePerPage: true,
        };

        return (
            <BootstrapTable data={this.props.data}
                            remote={true}
                            fetchInfo={{dataTotalSize: this.props.totalSize}}
                            pagination={true}
                            options={options}
                            hover={true}
                            bodyStyle={{overflow: 'overlay'}}
                            scrollTop={ 'Bottom'}
            >

                {this.props.columns.length === 0 ? <TableHeaderColumn key="1" dataField={"empty"}
                                                                      isKey={true}></TableHeaderColumn> :
                    this.props.columns.map(
                        c => <TableHeaderColumn key={c.columnName} dataField={c.fieldName} width='150px'
                                                isKey={c.isKey}
                                                dataSort={c.dataSort}>{c.columnName}</TableHeaderColumn>)}
            </BootstrapTable>
        );
    }
}
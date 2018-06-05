import Config from './config'

const c = new Config();

function handleErrors(response) {
    if (!(response.status === 200 || response.status === 204 )) {
        return response.json()
            .then(response => {
                return Promise.reject({code: response.status, message: response.message});
            })
            .catch(err => {
                throw err;
            });
    } else {
        return response.status === 200 ? response.json() : new Promise(function (resolve, reject) {
            resolve();
        });
    }
}

export default {
    fetchAll: (page, sizePerPage, sortOrder) => {
        return fetch(
            c.serverUrl + '/api/v1/list?page=' + page + '&size=' + sizePerPage + '&sort=name,' + sortOrder)
            .then(handleErrors);
    },
    uploadFile: (file, title, details) => {
        const url = c.serverUrl + '/api/v1/upload';
        const formData = new FormData();
        formData.append('file', file);
        formData.append('title', title);
        formData.append('details', details);
        const config = {
            headers: {
                'content-type': 'multipart/form-data'
            }
        };

        return fetch(url, {
            method: 'POST',
            body: formData,
            config: config
        })
            .then(handleErrors);


    }

}
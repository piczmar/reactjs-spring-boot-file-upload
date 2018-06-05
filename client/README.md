This project was bootstrapped with [Create React App](https://github.com/facebookincubator/create-react-app).

## Configure
The app requires only one configuration attribute, which is API server url and port.
Specify it in `config.js`.
Default is `serverUrl = "http://localhost:9080"`, which is configured for local development.

## Build and run

```
npm install
npm start
```

This will run application in develop mode with auto reload feature at `http://localhost:3000`.

## Distribute
To create production build run:

```
npm run build
```

This will create static files in folder `build` ready to be deployed on http server.
E.g. you may serve them using simple nodejs http server:

```
npm install -g serve
serve -s build
```



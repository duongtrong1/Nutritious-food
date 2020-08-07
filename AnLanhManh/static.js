const express = require('express');

const app = express();

app.use(express.static('./dist/static'));

console.log('Listening on localhost:8688');
app.listen(8688);
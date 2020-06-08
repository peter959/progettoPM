'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Authenticate to Algolia Database.
// TODO: Make sure you configure the `algolia.app_id` and `algolia.api_key` Google Cloud environment variables.
const algoliasearch = require('algoliasearch');
const client = algoliasearch(functions.config().algolia.algolia_app_id, functions.config().algolia.algolia_api_key);

// Name fo the algolia index for Blog posts content.
const ALGOLIA_QUEUES_INDEX_NAME = 'queues';

exports.addQueue = functions.database.ref('/queues/{queue_id}').onWrite((change, context) => {
  //const index = client.initIndex(ALGOLIA_QUEUES_INDEX_NAME);
  
  const afterData = change.after.val(); // data after the write

  const data = {
  	objectID: context.params.queue_id,
  	queue_description: afterData.queue_description,
  	queue_name: afterData.queue_name,
    queue_business: afterData.queue_business,
    queue_city: afterData.queue_city
  };

  return addToAlgolia(data, 'queues')
 	.then(res => console.log('SUCCESS ALGOLIA equipment ADD', res))
 	.catch(err => console.log('ERROR ALGOLIA equipment ADD', err));
});

exports.deleteQueue = functions.database.ref(`/queues/{queue_id}`).onDelete((snap, context) => {
  const index = client.initIndex(ALGOLIA_QUEUES_INDEX_NAME);
  const objectID = context.params.queue_id;
return index.deleteObject(objectID);
});

function addToAlgolia(object, indexName) {
 console.log('GETS IN addToAlgolia')
 console.log('object', object)
 console.log('indexName', indexName)
 const ALGOLIA_ID = functions.config().algolia.algolia_app_id;
 const ALGOLIA_ADMIN_KEY = functions.config().algolia.algolia_api_key;
 const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);
 const index = client.initIndex(indexName);
return new Promise((resolve, reject) => {
  index.saveObject(object)
  });
}

// Updates the search index when new blog entries are created or updated.
/*exports.indexentry = functions.database.ref('/queues/{queues_id}').onWrite(event => {
  const index = client.initIndex(ALGOLIA_QUEUES_INDEX_NAME);
  const afterData = event.data.val();
  const firebaseObject = {
    queue_business: afterData.data.queue_business,
    queue_city: afterData.data.queue_city
  };

  return index.saveObject(firebaseObject).then(
      () => event.data.adminRef.parent.child('last_index_timestamp').set(
          Date.parse(event.timestamp)));
});*/

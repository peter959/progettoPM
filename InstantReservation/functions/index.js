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

// Updates the search index when new blog entries are created or updated.
exports.indexentry = functions.database.ref('/queues/{queues_id}').onWrite(event => {
  const index = client.initIndex(ALGOLIA_QUEUES_INDEX_NAME);
  const firebaseObject = {
    queue_business: event.data.queue_business,
    queue_city: event.data.queue_city
  };

  return index.saveObject(firebaseObject).then(
      () => event.data.adminRef.parent.child('last_index_timestamp').set(
          Date.parse(event.timestamp)));
});

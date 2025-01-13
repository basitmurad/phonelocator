const functions = require("firebase-functions");

// Example function to greet a user
exports.greetUser = functions.https.onCall((data, context) => {
  const name = data.name;
  return {message: `Hello, ${name}!`};
});

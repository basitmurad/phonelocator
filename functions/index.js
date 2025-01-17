const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();
const db = admin.database();

exports.getChildDetailsWithPing =
functions.https.onCall(async (data, context) => {
  const {parentID, childID} = data;

  if (!parentID || !childID) {
    throw new functions.https.HttpsError(
        "invalid-argument",
        "ParentID and ChildID are required.",
    );
  }

  try {
    // Reference for child ping status and details
    const childPingRef = db.ref(`Connections/${parentID}/${childID}/ping`);
    const childDetailsRef = db.ref(`Connections/${parentID}/${childID}`);

    // Generate a unique ping ID
    const pingID = `ping_${Date.now()}`;

    // Send a ping request to the child app
    await childPingRef.set({
      pingID,
      requestedAt: new Date().toISOString(),
    });

    // Wait for the child app to respond
    const timeout = 10000; // Timeout after 10 seconds
    const childResponse = await new Promise((resolve, reject) => {
      const timeoutHandler = setTimeout(
          () => reject(new Error("Child app did not respond in time.")),
          timeout,
      );

      childDetailsRef.on("value", (snapshot) => {
        const data = snapshot.val();
        if (data && data.lastPingID === pingID) {
          clearTimeout(timeoutHandler);
          resolve(data);
        }
      });
    });

    // Extract and return the child details
    const details = {
      battery: childResponse.battery || null,
      childID: childResponse.childID || null,
      parentID: childResponse.parentID || null,
      date: childResponse.date || null,
      time: childResponse.time || null,
      latitude: childResponse.latitude || null,
      longitude: childResponse.longitude || null,
    };

    return {
      success: true,
      message: "Child details fetched successfully.",
      data: details,
    };
  } catch (error) {
    console.error("Error fetching child details:", error);
    throw new functions.https.HttpsError("unknown", error.message);
  }
});

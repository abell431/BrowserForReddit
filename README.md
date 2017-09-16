# BrowserForReddit

Android Nanodegree final (capstone) project.

Simple material designed reddit client for android. 

Description
Intended User
Features
User Interface Mocks
Screen 1
Screen 2
Key Considerations
How will your app handle data persistence?
Describe any corner cases in the UX.
Describe any libraries you’ll be using and share your reasoning for including them.
Next Steps: Required Tasks
Task 1: Project Setup
Task 2: Implement UI for Each Activity and Fragment
Task 3: Your Next Task
Task 4: Your Next Task
Task 5: Your Next Task
abell431
Browser for Reddit
Description
Context
Reddit.com is a social networking website where registered users can post content such as direct links or
text posts. These posts can be made in user made sections called subreddits. Registered users can
upvote or downvote posts which will make them more/less visible to other users.
Capstone_Stage1
2
Problem
The user is new to reddit and does not have an interest in many features like posting or voting and just
wants to read a small number of subreddits communities on a simple material designed app.
Solution
Design an app for Reddit.com that is lightweight and easy to choose what subreddits that should be
displayed. Users can select a post from the list to allow them to read the comments or interact with the
content of the post (Open a video link). Intents should be used to open content related to other apps
such as YouTube links
Intended User
New reddit user that has no intention to post anything or upvote/downvote and just wants to read a
custom set of subreddits. Could be a male/female of any age.
Features
 Fetches content from Reddit.com API
 Displays content in listview
 Allows content to be viewed in more detail including comments
 Content links can be shared outside the app
 Content will open using intents
 Banners ads shown at bottom of detail view
 Google analytics for tracking
User Interface Mocks
These can be created by hand (take a photo of your drawings and insert them in this flow), or using a
program like Photoshop or Balsamiq.
Main Activity Screen
Capstone_Stage1
3
This is the main activity and it contains a list view that displays the content from the chosen subreddits.
There is a navigation drawer which can be used to choose a subreddits or open the favorites that have
been saved
Capstone_Stage1
4
Detail View Activity Screen
This is the detail activity which opens when an article has been selected from the main list view activity.
It displays a large header image of the content which can be clicked to open the related website. The
comments can be viewed in a scroll view below.
Capstone_Stage1
5
Tablet Master-Detail Activity Screen
This is a mock screen for when the app is used on a tablet. It shows a master detail flow with the list
view and detail fragment both on display.
Key Considerations
How will your app handle data persistence?
A content provider will be created to manage the favorite’s database. SharedPreferences will be used to
store the user’s custom list of subreddits.
Capstone_Stage1
6
Describe any corner cases in the UX.
If there is no network the app will display a toast message warning the user. If a user enters an invalid
subreddit they will be warned by a toast message.
Describe any libraries you’ll be using and share your reasoning for including them.
Picasso will be used for image handling as it is a reliable library that I am familiar with from other
projects
Retrofit library will be used to simplify Reddit.com API calls
Next Steps: Required Tasks
This is the section where you can take the main features of your app (declared above) and decompose
them into tangible technical tasks that you can complete incrementally until you have a finished app.
Task 1: Project Setup
Configure Libraries
Picasso
Add compile 'com.squareup.picasso:picasso:2.5.2' to your dependencies block of the build.gradle for
the app
Retrofit
Add compile 'com.squareup.retrofit:retrofit:1.6.1' to your dependencies block of the build.gradle for
the app
Google Sevices
Add classpath 'com.google.gms:google-services:3.0.0' to the project level build.gradle.
Add apply plugin: 'com.google.gms.google-services' to the build.gradle for the app.
Google Firebase Ads
Add compile 'com.google.firebase:firebase-ads:9.0.0' to your dependencies block of the build.gradle for
the app
Google Play Services
Capstone_Stage1
7
Add compile 'com.google.android.gms:play-services-analytics:9.0.0' to your dependencies block of the
build.gradle for the app
Configure Permissions
Update your project's AndroidManifest.xml file to include the INTERNET, READ,
WRITE and ACCESS_NETWORK_STATE
Task 2: Implement UI for Each Activity and Fragment
 Build UI for Main Activity
 Build UI for Detail activity
 Build UI for Detail Fragment
 Build UI for Subreddit management Activity
 Build UI for Master Detail Tablet View
Task 3: Implement Reddit API
Implement Reddit.com API using retrofit. Documentation for the API can be found here:
https://www.reddit.com/dev/api . Implement the API so that only SFW content is retrieved
Task 4: Implement UI navigation
Setup button listeners with relevant intents to allow navigation from the main list view to the rest of the
app.
Task 5: Implement Google Firebase Ads
Log into Firebase console and retrieve google-services.json file. Give the app an add unit ID and then
place the Ad mob view inside the detail Activity
Task 6: Implement Google Analytics
Get the configuration file from https://developers.google.com
Add configuration file to the project and implement screen tracking to see when a user changes screen
in an app.
Task 7: Implement Master-Detail View
Capstone_Stage1
8
Create a master detail flow so that on tablets more information is displayed at one time when browsing
the app
Task 8: Implement SharedPreferences
In the subreddits management class implement a use of shared preferences to allow the user to choose
a custom list of subreddits
Task 4: Implement ContentProvider
Create a ContentProvider to use for saving favorites. Design contentURIs with appropriate structure and
authority then implement the content Provider class


App Name: Diet tracker
Language used: Kotlin
Devloping tool: Android Studio
devlopers: Rachal, Fuad, Daven

Minimum API: Android 24.0
Tested Device: Nexus 5X(API 31.0)
Ref: Black Board examples and slides proivded by Professor Yusuf Albayram 

Brife Description: It is a Diet/Meal tracker app that utilizes data from a REST API provided by FDC.
Primary purpose of it is: to inform use each time they consume any fooditem about its nuttient detail
and also keep track of calorie and macro nutrient consumed by the user throughout the day.It also informs users about their BMI Status and fat percentages so that they can make their own choice of what to eat and how much to eat. At the lanuch page, user is presented with a login page where he can insert his detail and proceed to the Nav Drawer. It has 3 parts: calorie segements shows the calorie consumed by him throughout the day and also presents a button to procced to the next page. Profile segment shows the health details of the users and user can also upload his image. Food segment presents the user with macro nutrient details that he consumsed throughout the day. From, calorie segment, when the button is clicked, user procced to the second screen where we can search for food in a searchbar. After searching that the nutrients of that particullar food is shown in a dialog box . Looking at the details, user can choose to add that food to his meal list or choose not to. If he chooses to add it, that food is entried into a a recycler view with its serving size details. At the same time, calories and macronutrients are updated along the same time. when user reaches 500 calorie marks it sends a notification sound about the percentage of the calorie consumed. When 2000 calorie mark is reached, user is presented with a notifcation to let him know that he consumed daily max calorie level.


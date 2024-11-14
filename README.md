	Thermal expansion calculator
Video url: TBD
Description:

	The app I have made is used to calculate thermal expansion of various materials. You can calculate expansion using size and temp change, temp needed for a size to expand a certain amount, and size needed for a certain temp change and expansion. The app is only intended for use for a couple hundred degrees around zero, coefficients change quite a bit out of that range. Should be fairly accurate close to zero degrees. App only works with coefficients of 10^-6 Kelvin.
	Having a bit of history with shrink fitting metals, I decided a calculator would be useful / interesting. Seemed like a decent option, as I wanted to make an app of some sort, and had thought of different design choices for quite a while. Therefore I created my app with Android Studio in Java.
	Starting out, I knew I wanted a data system that could be added to and removed from by the user. After a little research, I settled on a SQLite database for thermal expansion coefficients, and a shared preferences to store users preferred units of measurement. I watched a Youtube tutorial for Android Studio Java app development, then a Youtube tutorial for SQLite database in Android Studio.
With a fair bit of Googling and trial and error I got both functioning correctly.
	The I wanted the database to be usable weather the user wanted to use without much input, as well as usable for users who want to add more or more specific materials. Therefore on startup it checks if database is empty, if so, populates with most materials found on Wikipedia, then populates spinner. If it is not empty, populates spinner on calculator from existing database.
	My app has only 2 activities, main and settings. Main is where all calculations happen, settings is where you can change a few options.
	I knew I wanted to keep the app simple to use, so I made one page for the actual calculator, where you select the material type you want to calculate on a dropdown spinner, which saves/remembers last used material using shared preferences, then input any 2 of size, temp, or expansion wanted and it will put the result on the screen. 
	The only other activity is the settings page. It is a bit full, and is split into 4 different sections. The top is the main database controls. There is a button to delete all materials from database, with a confirm deletion screen, a button to add all materials I have hard coded into app into database, and a button to add all metals I have hardcoded into database.
	Next down on settings page is the user unit preferences. It would be possible to make the calculator without any units specified (besides temp), but I was worried that might cause a little confusion. I also thought would be useful to have different size and expansion units, due to expansion results often being several times smaller than the size units. There is options to change size and expansion between thou in, inch, foot, meter, mm, and temp options of C, F, and K. I set defaults to Inch, F, Inch. One button saves all selected options to a shared preferences file, sending a toast of "saved" to screen. I made the options that were selected on opening settings to current saved units, as I realized when testing it is annoying to select all 3 from options if you only want to change one.
	Next down is a section to add material. Simply has 2 lines, one for material name, one for coefficient of expansion. One button that saves to database, sending "saved" toast message when saved.
	Last section on settings page is a list of materials and their coefficients from the database. You can click on one to delete, with confirm deletion screen, then sends "deleted" toast message and refreshes list.
	The code for calculator is a little complicated with all user options available. I decided to only accept units of same measurement system, messier code and more chances of mistakes for very little benefit to mix measurement systems, imo at least. When calculate button pressed, calls cal function. First that happens is saving used material to shared preferences so material is selected next time open activity. Next it loads the size, temp, and expansion input from user, then sorts out to 3 different calculations, for calculating size, temp, or expansion, requiring 2 inputs. If more or less than 2 inputs entered, requests "Enter 2 values.
	After is sent to one of the 3 calculation function, saves both user inputs as well as material coefficient from database as floats. On size and expansion calculation functions, it then checks if user is using Fahrenheit, and converts to Kelvin, as coefficient is in Kelvin. Celsius is ignored, as it is directly related to Kelvin, just offset, which does not matter in this calculation, as we are calculating for temp change, not absolute temperature. It then goes through if else list checking for which units the user has selected. It than calculates using all inputs. In temp calculator section it then converts to F if user selected F, ignoring otherwise. It then prints result to screen, with users selected unit of measurement symbol. I also set maximum number of result digits to 7, seemed to be enough precision without taking up too much room. I also set so no exponents are used, as I have a hard time understanding, which took a fair bit of googling.
 	Icon was downloaded from flaticon.com - <a href="https://www.flaticon.com/free-icons/flame" title="flame icons">Flame icons created by Indah Rusiati - Flaticon</a>
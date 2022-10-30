Welcome to Weeh-Wooh!
by Andrew Ingram

 ---------- DESCRIPTION ----------
This program is designed to make an alert when on of three active
calls happens : Shootings, Stabbings, and Robberies

By default, it checks the BPD website every 2 minutes and collects
the latest 10 calls. (Both of these values can be changed)

 ---------- HOW TO USE ----------
Open Weeh-Wooh.jar by double clicking it.

To change how often the program checks BPD's website:
	- Use the spin-box at the top left of the screen labelled "Check Interval (Minutes)"
	- This time is measured in (integer, no decimal) minutes from 1 to 10.

To change how many calls are recorded per check:
	- Use the spin-box to the right of the Check Interval spin-box:
	- This accepts integer values from 1 to 20.

To stop the output box from scrolling automatically:
	- Use the "Automatic Ouput Scrolling" checkbox to the top left of the output box.
	- When checked, the output box will scroll to new messages.
	- When unchecked, the user may scroll freely without interruptions.

Ouput:
	- The output box is at the bottom of the screen. This is where messages about data
	are reported to the user.
	- By default, the output box automatically scrolls to the newest messages as soon
	as they appear. To stop this, use the "Automatic Output Scrolling" checkbox.

Details Secttion:
	- Contains information of the currently selected call including agency, service, nature,
	start time, and address.

Notifications:
	- When a shooting, stabbing, or robbery happens, a sound will play and it will be
	reported to the notification box on the right side of the screen. 
	- The user may View more details about the notification by pressing the "Show" button.
	This will bring the information of the call from the website to "Details" Section.
	- A notification can be dismissed by pressing the "Close" button to the left of the
	"Open" button.
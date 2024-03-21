# ComposeCronometer

ComposeCronometer is an Android application that demonstrates the usage of Jetpack Compose for building the UI, and the Kotlin Flows together with coroutines' Job to handle a countdown timer.

<img src="/screen1.png" alt="Screen 1" width="300">
<img src="/screen2.png" alt="Screen 2" width="300">
<img src="/screen3.png" alt="Screen 3" width="300">

## Purpose

ComposeCronometer is designed to showcase the integration of Jetpack Compose, Kotlin Flows, and coroutines to build a countdown timer feature. The app consists of a Cronometer built with Jetpack Compose components and a custom TimePickerDialog for setting the timer duration.

### Features

- **Cronometer UI:** Utilizes Jetpack Compose to create a user-friendly interface for displaying the countdown timer.
- **Custom TimePickerDialog:** Allows users to set the desired time duration for the countdown timer.
- **ViewModel Integration:** Handles the remaining time as a StateFlow, providing seamless updates to the UI.
- **Countdown Timer:** Utilizes coroutines' Job to execute a countdown timer in the background, updating the remaining time until it reaches zero.

### How it Works

1. **Setting up the Timer:** Users interact with the custom TimePickerDialog to set the desired countdown duration.
2. **Starting the Cronometer:** Upon initiating the countdown, the ViewModel triggers a coroutine Job to handle the countdown logic.
3. **Updating the UI:** The Cronometer UI continuously receives updates from the ViewModel's timeRemaining StateFlow, ensuring real-time display of the remaining time.
4. **Completion:** Once the countdown reaches zero, the Cronometer completes its operation, signaling the end of the timer.

### Usage

1. Launch the app and interact with the TimePickerDialog to set the countdown duration.
2. Start the Cronometer to begin the countdown.
3. Monitor the remaining time displayed on the Cronometer UI.
4. Upon completion, the Cronometer concludes its operation automatically.

## Contributing

Contributions to this project are welcome. Feel free to submit bug fixes, feature enhancements, or other improvements via pull requests.

## License

This project is licensed under the MIT License.

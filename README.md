Fieldworker
======

Fieldworker is an Android application for urban pedestrian field experiments, recording movement and events.

It is used in behavioural and mobile EEG research, using accurate timestamps to associate participant behaviour with other datasources.

It records behavioural events through a simple 8-button UI, and collects GPS coordinates.

Importantly, it records time with millisecond precision, both as human readable and as unix epoch format, for flexible post-processing.

#Instuctions

Create a folder "Fieldworkerio" in the root.
Create a file named "fieldworker_events.txt" with 8 words/short phrases to name the buttons. Each button should be on a new line ? maximum 8. Place this in the Fieldworker-io folder.

After use, new files will be in the "Fieldworker-io/data" subfolder.


#Development History

Fieldworker was first developped in Processing for Android by mandarini (then named 'Logger'; read more here http://psybercity.co.uk/2014/07/06/logger-_-an-android-app/) as part of MSc research.

Panos wrote the app in native Android for greater flexibility and future upgrades.

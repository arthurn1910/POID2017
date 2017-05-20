import QtQuick 2.7
import QtQuick.Controls 2.1
import QtQuick.Controls.Styles 1.4
import QtQuick.Dialogs 1.2
import QtCharts 2.0
import QtQuick.Layouts 1.3

import DataSource 0.1
import Amdf 0.1
import Fourier 0.1
import Player 0.1

ApplicationWindow {
    visible: true
    width: 1200
    height: 600
    title: qsTr("PoID Sound")

    Page {
        anchors.fill: parent

        Rectangle {
            id: pageBackground
            anchors.fill: parent
            color: "#AAAAAA"
        }

        Item {
            id: header
            anchors.left: parent.left
            anchors.right: parent.right
            anchors.top: parent.top

            anchors.leftMargin: parent.height * 0.02
            anchors.rightMargin: parent.height * 0.02
            anchors.topMargin: parent.height * 0.02
            anchors.bottomMargin: parent.height * 0.02

            height: parent.height * 0.1

            Rectangle {
                anchors.fill: parent
                color: "#EEEEEE"
            }

            Row {
                id: layout

                anchors.left: parent.left
                anchors.top: parent.top
                anchors.bottom: parent.bottom

                anchors.leftMargin: parent.height * 0.16
                anchors.topMargin: parent.height * 0.16

                spacing: 6
                Button {
                    id: openFileButton
                    text: "Open new file"
                    visible: false
                    background: Rectangle {
                        implicitWidth: 100
                        implicitHeight: 40
                        color: parent.down ? "#999999" : "#DDDDDD"
                    }
                    onClicked: {
                        fileDialog.open()
                    }
                }
                Button {
                    text: "Save file"
                    visible: false
                    background: Rectangle {
                        implicitWidth: 100
                        implicitHeight: 40
                        color: parent.down ? "#999999" : "#DDDDDD"
                    }
                    onClicked: {
                    }
                }
                Button {
                    text: "Select header"
                    background: Rectangle {
                        implicitWidth: 100
                        implicitHeight: 40
                        color: parent.down ? "#999999" : "#DDDDDD"
                    }
                    onClicked: {
                        soundHeaderDialog.open()
                    }
                }

                Text {
                    id: fileName
                    text: "N/A"
                    font.pointSize: 15
                }

                FileDialog {
                    id: fileDialog
                    title: "Please choose a file"
                    folder: shortcuts.home
                    nameFilters: [ "Music files (*.wav)", "All files (*)" ]
                    selectedNameFilter: "Music files (*.wav)"
                    onAccepted: {
                        data.loadSoundData(fileDialog.fileUrl)
                        fileName.text = fileDialog.fileUrl
                        content.visible = true
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                }


                FileDialog {
                    id: soundHeaderDialog
                    title: "Please choose a file"
                    folder: shortcuts.home
                    nameFilters: [ "Music files (*.wav)", "All files (*)" ]
                    selectedNameFilter: "Music files (*.wav)"
                    onAccepted: {
                        data.loadSoundHeader(soundHeaderDialog.fileUrl)
                        openFileButton.visible = true
                    }
                    onRejected: {
                        console.log("Canceled")
                    }
                }
            }
        }

        Item {
            id: content
            visible: false
            anchors.left: parent.left
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            anchors.top: header.bottom

            anchors.leftMargin: parent.height * 0.02
            anchors.rightMargin: parent.height * 0.02
            anchors.topMargin: parent.height * 0.02
            anchors.bottomMargin: parent.height * 0.02

            Rectangle {
                id: contentBackground
                anchors.fill: parent
                color: "#EEEEEE"
            }

            DataSource {
                id: data
                Component.onCompleted: {
                    var inputSeries = inputChart.createSeries(ChartView.SeriesTypeLine, "Input", inputXAxis, inputYAxis);
                    data.initInputChart(inputSeries, inputXAxis, inputYAxis)
                }

                onDataLoaded: {
                    player.setAudioHeader(data)
                    amdf.setData(data)
                    fourier.setData(data)
                }
            }

            Amdf {
                id:amdf
                Component.onCompleted: {
                    var amdfSeries = amdfChart.createSeries(ChartView.SeriesTypeLine, "Input", amdfXAxis, amdfYAxis);
                    amdf.initAmdfChart(amdfSeries, amdfXAxis, amdfYAxis)
                }

                onAmdfFinished: {
                    amdfMinimumValue.text = amdf.amdfBasicFrequencyMinimum
                    amdfMedianValue.text = amdf.amdfBasicFrequencyMedian
                }
            }

            Fourier {
                id: fourier
                Component.onCompleted: {
                    var fourierSeries = fourierChart.createSeries(ChartView.SeriesTypeLine, "Input", fourierXAxis, fourierYAxis);
                    fourier.initFourierChart(fourierSeries, fourierXAxis, fourierYAxis)
                }

                onFourierFinished: {
                    furierMedianValue.text = fourier.basicFrequency
                }
            }

            Player {
                id: player

                Component.onCompleted: {
                    var generatedSeries = generatedChart.createSeries(ChartView.SeriesTypeLine, "Generated", generatedXAxis, generatedYAxis);
                    player.initGeneratedChart(generatedSeries, generatedXAxis, generatedYAxis)
                }
            }

            ChartView {
                id: inputChart
                title: "Input sound"
                antialiasing: true
                legend.visible: false

                anchors.left: parent.left
                anchors.top: parent.top

                anchors.leftMargin: parent.height * 0.02
                anchors.topMargin: parent.height * 0.02

                width: parent.width * 0.6
                height: parent.height * 0.4

                ValueAxis {
                    id: inputYAxis
                }

                ValueAxis {
                    id: inputXAxis
                }
            }

            Item {
                id: inputChartNavi
                anchors.left: inputChart.right
                anchors.top: inputChart.top
                anchors.right: tools.left

                anchors.leftMargin: parent.height * 0.02
                anchors.topMargin: parent.height * 0.03

                height: parent.height * 0.2

                Row {
                    id: inputMagnitudeRow
                    anchors.left: parent.left
                    anchors.top: parent.top

                    anchors.bottomMargin: parent.height * 0.05

                    spacing: 6
                    Button {
                        text: " + "
                        background: Rectangle {
                            color: parent.down ? "#999999" : "#DDDDDD"
                        }
                        onClicked: {
                            data.increaseInputMagnitude()
                        }
                    }
                    Button {
                        text: " -- "
                        background: Rectangle {
                            color: parent.down ? "#999999" : "#DDDDDD"
                        }
                        onClicked: {
                            data.decreaseInputMagnitude()
                        }
                    }
                }
                Slider  {
                    id: inputOffsetSlider
                    anchors.left: parent.left
                    anchors.top: inputMagnitudeRow.bottom

                    width: inputMagnitudeRow.width
                    anchors.topMargin: parent.width * 0.1
                    anchors.rightMargin: parent.width * 0.1

                    from: 0
                    value: 0
                    to: 1
                    onValueChanged: {
                        data.inputOffsetChanged(position)
                    }
                }

                Button {
                    anchors.top: inputOffsetSlider.bottom

                    anchors.topMargin: parent.height * 0.1

                    text: "-- PLAY --"
                    background: Rectangle {
                        color: parent.down ? "#999999" : "#DDDDDD"
                    }
                    onClicked: {
                        player.setData(data)
                        player.play()
                    }
                }
            }

            Item {
                id: stackChart
                anchors.left: parent.left
                anchors.top: inputChart.bottom

                anchors.leftMargin: parent.height * 0.02
                anchors.bottomMargin: parent.height * 0.02

                width: parent.width * 0.7
                height: parent.height * 0.6

                TabBar {
                    id: bar
                    width: parent.width
                    TabButton {
                        text: qsTr("AMDF")
                    }
                    TabButton {
                        text: qsTr("FOURIER")
                    }
                    TabButton {
                        text: qsTr("GENERATED")
                    }
                }

                StackLayout {
                    id: stackChartLayout
                    currentIndex: bar.currentIndex
                    anchors.left: parent.left
                    anchors.right: parent.right
                    anchors.top: bar.bottom
                    anchors.bottom: parent.bottom

                    Item {
                        id: amdfStackLayout
                        anchors.fill: parent

                        ChartView {
                            id: amdfChart
                            title: "AMDF"
                            antialiasing: true
                            legend.visible: false

                            width: parent.width * 0.87
                            height: parent.height * 0.96

                            ValueAxis {
                                id: amdfYAxis
                            }

                            ValueAxis {
                                id: amdfXAxis
                            }
                        }

                        Item {
                            id: amdfChartNavi
                            anchors.left: amdfChart.right
                            anchors.top: amdfChart.top

                            anchors.leftMargin: parent.height * 0.02
                            anchors.topMargin: parent.height * 0.03

                            height: parent.height * 0.4

                            Row {
                                id: amdfMagnitudeRow
                                anchors.left: parent.left
                                anchors.top: parent.top

                                anchors.bottomMargin: parent.height * 0.05

                                spacing: 6
                                Button {
                                    text: " + "
                                    background: Rectangle {
                                        color: parent.down ? "#999999" : "#DDDDDD"
                                    }
                                    onClicked: {
                                        amdf.increaseAmdfMagnitude()
                                    }
                                }
                                Button {
                                    text: " -- "
                                    background: Rectangle {
                                        color: parent.down ? "#999999" : "#DDDDDD"
                                    }
                                    onClicked: {
                                        amdf.decreaseAmdfMagnitude()
                                    }
                                }
                            }

                            Slider  {
                                id: amdfOffsetSlider
                                anchors.left: parent.left
                                anchors.top: amdfMagnitudeRow.bottom

                                width: amdfMagnitudeRow.width
                                anchors.topMargin: parent.width * 0.1
                                anchors.rightMargin: parent.width * 0.1

                                from: 0
                                value: 0
                                to: 1
                                onValueChanged: {
                                    amdf.amdfOffsetChanged(position)
                                }
                            }
                        }
                    }

                    Item {
                        id: fourierStackLayout
                        anchors.fill: parent
                        ChartView {
                            id: fourierChart
                            title: "Fourier"
                            antialiasing: true
                            legend.visible: false

                            width: parent.width * 0.87
                            height: parent.height * 0.96

                            ValueAxis {
                                id: fourierYAxis
                            }

                            ValueAxis {
                                id: fourierXAxis
                            }
                        }

                        Item {
                            id: fourierChartNavi
                            anchors.left: fourierChart.right
                            anchors.top: fourierChart.top

                            anchors.leftMargin: parent.height * 0.02
                            anchors.topMargin: parent.height * 0.03

                            height: parent.height * 0.4

                            Row {
                                id: fourierMagnitudeRow
                                anchors.left: parent.left
                                anchors.top: parent.top

                                anchors.bottomMargin: parent.height * 0.05

                                spacing: 6
                                Button {
                                    text: " + "
                                    background: Rectangle {
                                        color: parent.down ? "#999999" : "#DDDDDD"
                                    }
                                    onClicked: {
                                        fourier.increaseFourierMagnitude()
                                    }
                                }
                                Button {
                                    text: " -- "
                                    background: Rectangle {
                                        color: parent.down ? "#999999" : "#DDDDDD"
                                    }
                                    onClicked: {
                                        fourier.decreaseFourierMagnitude()
                                    }
                                }
                            }

                            Slider  {
                                id: fourierOffsetSlider
                                anchors.left: parent.left
                                anchors.top: fourierMagnitudeRow.bottom

                                width: fourierMagnitudeRow.width
                                anchors.topMargin: parent.width * 0.1
                                anchors.rightMargin: parent.width * 0.1

                                from: 0
                                value: 0
                                to: 1
                                onValueChanged: {
                                    fourier.fourierOffsetChanged(position)
                                }
                            }
                        }
                    }

                    Item {
                        id: generatedStackLayout
                        anchors.fill: parent

                        ChartView {
                            id: generatedChart
                            title: "GENERATED"
                            antialiasing: true
                            legend.visible: false

                            width: parent.width * 0.87
                            height: parent.height * 0.96

                            ValueAxis {
                                id: generatedYAxis
                            }

                            ValueAxis {
                                id: generatedXAxis
                            }
                        }

                        Item {
                            id: generatedChartNavi
                            anchors.left: generatedChart.right
                            anchors.top: generatedChart.top

                            anchors.leftMargin: parent.height * 0.02
                            anchors.topMargin: parent.height * 0.03

                            height: parent.height * 0.4

                            Row {
                                id: generatedMagnitudeRow
                                anchors.left: parent.left
                                anchors.top: parent.top

                                anchors.bottomMargin: parent.height * 0.05

                                spacing: 6
                                Button {
                                    text: " + "
                                    background: Rectangle {
                                        color: parent.down ? "#999999" : "#DDDDDD"
                                    }
                                    onClicked: {
                                        player.increaseGeneratedMagnitude()
                                    }
                                }
                                Button {
                                    text: " -- "
                                    background: Rectangle {
                                        color: parent.down ? "#999999" : "#DDDDDD"
                                    }
                                    onClicked: {
                                        player.decreaseGeneratedMagnitude()
                                    }
                                }
                            }

                            Slider  {
                                id: generatedOffsetSlider
                                anchors.left: parent.left
                                anchors.top: generatedMagnitudeRow.bottom

                                width: generatedMagnitudeRow.width
                                anchors.topMargin: parent.width * 0.1
                                anchors.rightMargin: parent.width * 0.1

                                from: 0
                                value: 0
                                to: 1
                                onValueChanged: {
                                    player.generatedOffsetChanged(position)
                                }
                            }
                        }
                    }
                }
            }

            Item {
                id: tools
                anchors.right: parent.right
                anchors.top: parent.top
                anchors.bottom: parent.bottom

                anchors.leftMargin: parent.height * 0.02
                anchors.rightMargin: parent.height * 0.02
                anchors.topMargin: parent.height * 0.02
                anchors.bottomMargin: parent.height * 0.02

                width: parent.width * 0.3

                Rectangle {
                    id: toolsBackround
                    anchors.fill: parent
                    border.color: "black"
                    border.width: 1
                }

                TabBar {
                    id: toolsBar
                    width: parent.width

                    TabButton {
                        text: "Single tone"
                    }
                    TabButton {
                        text: "Multi tone"
                    }
                }

                StackLayout {
                    anchors.top: toolsBar.bottom
                    anchors.left: parent.left
                    anchors.right: parent.right
                    anchors.bottom: parent.bottom
                    currentIndex: toolsBar.currentIndex

                    Item {
                        id: singleToneTools

                        Item {
                            id: amdfTool
                            anchors.right: parent.right
                            anchors.top: parent.top
                            anchors.left: parent.left

                            height: parent.height * 0.3

                            Rectangle {
                                anchors.fill: parent
                                border.color: "black"
                                border.width: 1
                            }

                            Text {
                                id: amdfHeader

                                anchors.top: parent.top
                                anchors.left: parent.left

                                anchors.leftMargin: parent.width * 0.02
                                anchors.topMargin: parent.width * 0.02

                                text: "AMDF"
                                font.pointSize: 20
                            }

                            Item {
                                id: amdfMinimumRow
                                anchors.left: parent.left
                                anchors.top: amdfHeader.bottom
                                anchors.leftMargin: parent.width * 0.02
                                anchors.topMargin: parent.width * 0.02
                                anchors.bottomMargin: parent.width * 0.02

                                height: parent.height * 0.1

                                Text {
                                    id: amdfMinimumLabel

                                    anchors.left: parent.left

                                    font.pointSize: 10
                                    text: "Basic frequency (first): "
                                }

                                Text {
                                    id: amdfMinimumValue
                                    anchors.left: amdfMinimumLabel.right

                                    anchors.leftMargin: parent.height * 0.02

                                    text: "N/A"
                                }
                            }

                            Item {
                                id: amdfMedianRow
                                anchors.left: parent.left
                                anchors.top: amdfMinimumRow.bottom
                                anchors.leftMargin: parent.width * 0.02
                                anchors.topMargin: parent.width * 0.02
                                anchors.bottomMargin: parent.width * 0.02

                                height: parent.height * 0.1

                                Text {
                                    id: amdfMedianLabel

                                    anchors.left: parent.left

                                    font.pointSize: 10
                                    text: "Basic frequency (median): "
                                }

                                Text {
                                    id: amdfMedianValue
                                    anchors.left: amdfMedianLabel.right

                                    anchors.leftMargin: parent.height * 0.02

                                    text: "N/A"
                                }
                            }

                            Item {
                                id: optimalizationRow
                                anchors.left: parent.left
                                anchors.top: amdfMedianRow.bottom
                                anchors.leftMargin: parent.width * 0.02
                                anchors.topMargin: parent.width * 0.02
                                anchors.bottomMargin: parent.width * 0.02

                                height: parent.height * 0.1

                                Text {
                                    id: optimalizationLabel

                                    anchors.left: parent.left

                                    font.pointSize: 10
                                    text: "Optimalization: "
                                }

                                Rectangle {
                                    border.color: "black"
                                    border.width: 1

                                    anchors.left: optimalizationLabel.right

                                    anchors.leftMargin: parent.height * 0.02

                                    height: parent.height * 1.2
                                    width: amdfTool.width * 0.1

                                    TextInput {
                                        id: optimalizationValue
                                        anchors.fill: parent
                                        anchors.leftMargin: parent.height * 0.1
                                        anchors.rightMargin: parent.height * 0.1
                                        anchors.topMargin: parent.height * 0.1
                                        anchors.bottomMargin: parent.height * 0.1

                                        text: "10"
                                        onTextChanged: {
                                            amdf.amdfOptimalizationParam = text
                                        }
                                    }
                                }
                            }

                            Button {
                                id: amdfPlayButton
                                visible: false
                                anchors.right: parent.right
                                anchors.bottom: amdfButton.top
                                anchors.rightMargin: parent.width * 0.02
                                text: "Play sample"
                                onClicked: {
                                    player.generateSoundSample(amdf.amdfBasicFrequencyMedian, amdf.amplitude)
                                    player.play()
                                }
                            }

                            Button {
                                id: amdfButton
                                anchors.right: parent.right
                                anchors.bottom: parent.bottom
                                anchors.rightMargin: parent.width * 0.02
                                anchors.bottomMargin: parent.width * 0.02
                                text: "AMDF"
                                onClicked: {
                                    amdf.runAMDF()
                                    amdfPlayButton.visible = true
                                }
                            }
                        }

                        Item {
                            id: fourierTool
                            anchors.right: parent.right
                            anchors.top: amdfTool.bottom
                            anchors.left: parent.left

                            height: parent.height * 0.7

                            Rectangle {
                                anchors.fill: parent
                                border.color: "black"
                                border.width: 1
                            }

                            Text {
                                id: fourierHeader

                                anchors.top: parent.top
                                anchors.left: parent.left

                                anchors.leftMargin: parent.width * 0.02
                                anchors.topMargin: parent.width * 0.02

                                text: "Fourier"
                                font.pointSize: 20
                            }

                            Item {
                                id: fourierMedianRow
                                anchors.left: parent.left
                                anchors.top: fourierHeader.bottom
                                anchors.leftMargin: parent.width * 0.02

                                height: parent.height * 0.1

                                Text {
                                    id: furierMedianLabel

                                    anchors.left: parent.left

                                    font.pointSize: 10
                                    text: "Basic frequency (most similar): "
                                }

                                Text {
                                    id: furierMedianValue
                                    anchors.left: furierMedianLabel.right

                                    anchors.leftMargin: parent.height * 0.02

                                    text: "N/A"
                                }
                            }

                            Item {
                                id: threasholdRow
                                anchors.left: parent.left
                                anchors.top: fourierMedianRow.bottom
                                anchors.leftMargin: parent.width * 0.02

                                height: parent.height * 0.1

                                Text {
                                    id: threasholdLabel

                                    anchors.left: parent.left

                                    font.pointSize: 10
                                    text: "Log threashold: "
                                }

                                Rectangle {
                                    border.color: "black"
                                    border.width: 1

                                    anchors.left: threasholdLabel.right

                                    anchors.leftMargin: parent.height * 0.02

                                    height: parent.height * 1
                                    width: amdfTool.width * 0.1

                                    TextInput {
                                        id: threasholdValue
                                        anchors.fill: parent
                                        anchors.leftMargin: parent.height * 0.1
                                        anchors.rightMargin: parent.height * 0.1
                                        anchors.topMargin: parent.height * 0.1
                                        anchors.bottomMargin: parent.height * 0.1

                                        text: "8"
                                        onTextChanged: {
                                            fourier.threasholdParam = text
                                        }
                                    }
                                }
                            }

                            ButtonGroup {
                                buttons: column.children
                                onClicked: fourier.setWindowType(button.text)
                            }

                            Column {
                                id: column
                                anchors.top: threasholdRow.bottom
                                anchors.left: parent.left
                                height: parent.height * 0.3
                                width: parent.width * 0.6

                                anchors.leftMargin: parent.width * 0.05
                                spacing: -10

                                Label {
                                    text: qsTr("Window:")
                                }

                                RadioButton {
                                    checked: true
                                    text: qsTr("Gauss")
                                }

                                RadioButton {
                                    text: qsTr("Hamming 1")
                                }

                                RadioButton {
                                    text: qsTr("Hamming 2")
                                }

                                RadioButton {
                                    text: qsTr("Bartlett")
                                }
                            }

                            Button {
                                id: furierPlayButton
                                visible: false
                                anchors.right: parent.right
                                anchors.bottom: fourierButton.top
                                anchors.rightMargin: parent.width * 0.02
                                text: "Play sample"
                                onClicked: {
                                    player.generateSoundSample(fourier.basicFrequency, fourier.amplitude)
                                    player.play()
                                }
                            }

                            Button {
                                id: fourierButton
                                anchors.right: parent.right
                                anchors.bottom: parent.bottom
                                anchors.rightMargin: parent.width * 0.02
                                anchors.bottomMargin: parent.width * 0.02
                                text: "FOURIER"
                                onClicked: {
                                    fourier.runFOURIER()
                                    furierPlayButton.visible = true
                                }
                            }
                        }
                    }

                    Item {
                        id: multiToneTools
                        anchors.fill: parent

                        Item {
                            id: windowRow
                            anchors.left: parent.left
                            anchors.top: parent.top
                            anchors.leftMargin: parent.width * 0.02
                            anchors.topMargin: parent.width * 0.02
                            anchors.bottomMargin: parent.width * 0.02

                            height: parent.height * 0.05

                            Text {
                                id: windowLabel

                                anchors.left: parent.left

                                font.pointSize: 10
                                text: "Window size: "
                            }

                            Rectangle {
                                border.color: "black"
                                border.width: 1

                                anchors.left: windowLabel.right

                                anchors.leftMargin: parent.width * 0.02

                                height: parent.height * 1
                                width: amdfTool.width * 0.1

                                TextInput {
                                    id: windowValue
                                    anchors.fill: parent
                                    anchors.leftMargin: parent.height * 0.1
                                    anchors.rightMargin: parent.height * 0.1
                                    anchors.topMargin: parent.height * 0.1
                                    anchors.bottomMargin: parent.height * 0.1

                                    text: "16384"
                                    onTextChanged: {
                                        fourier.windowParam = text
                                    }
                                }
                            }
                        }

                        Item {
                            id: frequencyRow
                            anchors.left: parent.left
                            anchors.top: windowRow.bottom
                            anchors.leftMargin: parent.width * 0.02
                            anchors.topMargin: parent.width * 0.02
                            anchors.bottomMargin: parent.width * 0.02

                            height: parent.height * 0.05

                            Text {
                                id: frequencyLabel

                                anchors.left: parent.left

                                font.pointSize: 10
                                text: "Frequency threshold (%): "
                            }

                            Rectangle {
                                border.color: "black"
                                border.width: 1

                                anchors.left: frequencyLabel.right

                                anchors.leftMargin: parent.width * 0.02

                                height: parent.height * 1
                                width: multiToneTools.width * 0.05

                                TextInput {
                                    id: frequencyValue
                                    anchors.fill: parent
                                    anchors.leftMargin: parent.height * 0.1
                                    anchors.rightMargin: parent.height * 0.1
                                    anchors.topMargin: parent.height * 0.1
                                    anchors.bottomMargin: parent.height * 0.1

                                    text: "10"
                                    onTextChanged: {
                                        fourier.frequencyThreshold = text
                                    }
                                }
                            }
                        }

                        Item {
                            id: threasholdMultiRow
                            anchors.left: parent.left
                            anchors.top: frequencyRow.bottom
                            anchors.leftMargin: parent.width * 0.02

                            height: parent.height * 0.05

                            Text {
                                id: threasholdMultiLabel

                                anchors.left: parent.left

                                font.pointSize: 10
                                text: "Log threashold: "
                            }

                            Rectangle {
                                border.color: "black"
                                border.width: 1

                                anchors.left: threasholdMultiLabel.right

                                anchors.leftMargin: parent.height * 0.02

                                height: parent.height
                                width: multiToneTools.width * 0.1

                                TextInput {
                                    id: threasholdMultiValue
                                    anchors.fill: parent
                                    anchors.leftMargin: parent.height * 0.1
                                    anchors.rightMargin: parent.height * 0.1
                                    anchors.topMargin: parent.height * 0.1
                                    anchors.bottomMargin: parent.height * 0.1

                                    text: "5"
                                    onTextChanged: {
                                        fourier.threasholdParam = text
                                    }
                                }
                            }
                        }

                        Button {
                            id: fourierMultiPlayButton
                            visible: false
                            anchors.right: parent.right
                            anchors.top: fourierMultiButton.bottom
                            anchors.rightMargin: parent.width * 0.02
                            text: "Play sample"
                            onClicked: {
                                player.generateSoundMultiSample(fourier)
                                player.play()
                            }
                        }

                        Button {
                            id: fourierMultiButton
                            anchors.right: parent.right
                            anchors.top: parent.top
                            anchors.rightMargin: parent.width * 0.02
                            anchors.bottomMargin: parent.width * 0.02
                            text: "FOURIER multi"
                            onClicked: {
                                fourier.runFOURIERmultiTone()
                                fourierMultiPlayButton.visible = true
                            }
                        }
                    }
                }
            }
        }
    }
}

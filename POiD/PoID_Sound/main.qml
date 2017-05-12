import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0
import QtQuick.Controls.Styles 1.4
import QtQuick.Dialogs 1.2
import QtCharts 2.0
import DataSource 0.1

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
                    text: "Open new file"
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
                    background: Rectangle {
                        implicitWidth: 100
                        implicitHeight: 40
                        color: parent.down ? "#999999" : "#DDDDDD"
                    }
                    onClicked: {
                        fileDialog.Save
                    }
                }

                FileDialog {
                    id: fileDialog
                    title: "Please choose a file"
                    folder: shortcuts.home
                    nameFilters: [ "Music files (*.wav *.mp3)", "All files (*)" ]
                    selectedNameFilter: "Music files (*.wav *.mp3)"
                    onAccepted: {
                        data.loadSoundData(fileDialog.fileUrl)
                        content.visible = true
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
                    var outputSeries = outputChart.createSeries(ChartView.SeriesTypeLine, "Input", outputXAxis, outputYAxis);
                    data.initOutputChart(outputSeries, outputXAxis, outputYAxis)
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
                height: parent.height * 0.48

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
                        data.playInput()
                    }
                }
            }

            ChartView {
                id: outputChart
                title: "Output sound"
                antialiasing: true
                legend.visible: false

                anchors.left: parent.left
                anchors.top: inputChart.bottom

                anchors.leftMargin: parent.height * 0.02
                anchors.bottomMargin: parent.height * 0.02

                width: parent.width * 0.6
                height: parent.height * 0.48

                ValueAxis {
                    id: outputYAxis
                }

                ValueAxis {
                    id: outputXAxis
                }
            }

            Item {
                id: outputChartNavi
                anchors.left: outputChart.right
                anchors.top: outputChart.top

                anchors.leftMargin: parent.height * 0.02
                anchors.topMargin: parent.height * 0.03

                height: parent.height * 0.2

                Row {
                    id: outputMagnitudeRow
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
                        }
                    }
                    Button {
                        text: " -- "
                        background: Rectangle {
                            color: parent.down ? "#999999" : "#DDDDDD"
                        }
                        onClicked: {
                        }
                    }
                }

                Row {
                    id: outputOffsetRow
                    anchors.left: parent.left
                    anchors.top: outputMagnitudeRow.bottom

                    anchors.topMargin: parent.height * 0.1

                    spacing: 6
                    Button {
                        text: "<<"
                        background: Rectangle {
                            color: parent.down ? "#999999" : "#DDDDDD"
                        }
                        onClicked: {
                        }
                    }
                    Button {
                        text: ">>"
                        background: Rectangle {
                            color: parent.down ? "#999999" : "#DDDDDD"
                        }
                        onClicked: {
                        }
                    }
                }

                Button {
                    anchors.top: outputOffsetRow.bottom

                    anchors.topMargin: parent.height * 0.1

                    text: "-- PLAY --"
                    background: Rectangle {
                        color: parent.down ? "#999999" : "#DDDDDD"
                    }
                    onClicked: {
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
                    anchors.fill: parent
                    border.color: "black"
                    border.width: 1
                }
            }
        }
    }
}

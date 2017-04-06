#-------------------------------------------------
#
# Project created by QtCreator 2017-03-07T09:09:29
#
#-------------------------------------------------

QT       += core gui charts

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Zadanie_1
TEMPLATE = app

# The following define makes your compiler emit warnings if you use
# any feature of Qt which as been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0


SOURCES += main.cpp\
        mainwindow.cpp \
    tools/tool.cpp \
    customtabforimage.cpp \
    tools/zadanie1/brightness.cpp \
    tools/zadanie1/contrast.cpp \
    tools/zadanie1/filtr_linear_splot.cpp \
    tools/zadanie1/filtr_mean.cpp \
    tools/zadanie1/filtr_median.cpp \
    tools/zadanie1/filtr_sobel.cpp \
    tools/zadanie1/histogram.cpp \
    tools/zadanie1/negative.cpp \
    tools/tool_fourier.cpp \
    tools/utils.cpp \
    tools/zadanie2/low_high_pass_filter.cpp \
    tools/zadanie2/spectrum_window.cpp \
    tools/zadanie2/band_pass_stop_filter.cpp \
    tools/zadanie2/edge_detection_filter.cpp

HEADERS  += mainwindow.h \
    tools/tool.h \
    customtabforimage.h \ 
    tools/zadanie1/brightness.h \
    tools/zadanie1/contrast.h \
    tools/zadanie1/filtr_linear_splot.h \
    tools/zadanie1/filtr_mean.h \
    tools/zadanie1/filtr_median.h \
    tools/zadanie1/filtr_sobel.h \
    tools/zadanie1/histogram.h \
    tools/zadanie1/negative.h \
    tools/tool_fourier.h \
    tools/utils.h \
    tools/zadanie2/low_high_pass_filter.h \
    tools/zadanie2/spectrum_window.h \
    tools/zadanie2/band_pass_stop_filter.h \
    tools/zadanie2/edge_detection_filter.h

FORMS    += mainwindow.ui \
    customtabforimage.ui \
    tools/zadanie1/brightness.ui \
    tools/zadanie1/contrast.ui \
    tools/zadanie1/filtr_linear_splot.ui \
    tools/zadanie1/filtr_mean.ui \
    tools/zadanie1/filtr_median.ui \
    tools/zadanie1/filtr_sobel.ui \
    tools/zadanie1/histogram.ui \
    tools/zadanie1/negative.ui \
    tools/zadanie2/low_high_pass_filter.ui \
    tools/zadanie2/spectrum_window.ui \
    tools/zadanie2/band_pass_stop_filter.ui \
    tools/zadanie2/edge_detection_filter.ui

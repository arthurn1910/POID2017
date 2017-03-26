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
    tools/brightness.cpp \
    customtabforimage.cpp \
    tools/contrast.cpp \
    tools/negative.cpp \
    tools/filtr_mean.cpp \
    tools/filtr_median.cpp \
    tools/utils.cpp \
    tools/histogram.cpp \
    tools/filtr_linear_splot.cpp \
    tools/filtr_sobel.cpp

HEADERS  += mainwindow.h \
    tools/tool.h \
    tools/brightness.h \
    customtabforimage.h \
    tools/contrast.h \
    tools/negative.h \
    tools/filtr_mean.h \
    tools/filtr_median.h \
    tools/utils.h \
    tools/histogram.h \
    tools/filtr_linear_splot.h \
    tools/filtr_sobel.h

FORMS    += mainwindow.ui \
    customtabforimage.ui \
    tools/brightness.ui \
    tools/contrast.ui \
    tools/negative.ui \
    tools/filtr_mean.ui \
    tools/filtr_median.ui \
    tools/histogram.ui \
    tools/filtr_linear_splot.ui \
    tools/filtr_sobel.ui

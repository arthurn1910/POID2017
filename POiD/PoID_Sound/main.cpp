#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlFileSelector>
#include <QApplication>
#include "datasource.h"


int main(int argc, char *argv[])
{
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
    QApplication app(argc, argv);

    QQmlApplicationEngine engine;
    //QQmlFileSelector* selector = new QQmlFileSelector(&engine);

     qmlRegisterType<DataSource>("DataSource", 0, 1, "DataSource");

    engine.load(QUrl(QLatin1String("qrc:/main.qml")));

    return app.exec();
}

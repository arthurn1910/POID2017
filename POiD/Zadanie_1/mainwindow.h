#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QFileDialog>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
private slots:
    void on_actionFile_triggered();

    void on_tabWidget_tabCloseRequested(int index);

    void on_actionSaveProcessedImage_triggered();

private:
    void initializeImageFileDialog(QFileDialog &dialog, QFileDialog::AcceptMode acceptMode);
    QString selectNewFile();
    Ui::MainWindow *ui;
};

#endif // MAINWINDOW_H

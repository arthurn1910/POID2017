#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "customtabforimage.h"
#include <QVBoxLayout>
#include <QFileDialog>
#include <iostream>
#include <QStandardPaths>
#include <QImageWriter>
#include <QImageReader>
#include <QErrorMessage>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_actionFile_triggered()
{
    QString newFilePath = this->selectNewFile();

    if (newFilePath != NULL) {
        CustomTabForImage *newTab = new CustomTabForImage(ui->tabWidget, newFilePath);
        ui->tabWidget->addTab(newTab, newFilePath.split("/").last());
        ui->tabWidget->setCurrentWidget(newTab);
    }
}

void MainWindow::on_actionSaveProcessedImage_triggered()
{
    QString fileName = QFileDialog::getSaveFileName(this, "Zapisz", "", ".bmp");
    if (fileName.size() == 0) {
        return;
    }

    if (!fileName.endsWith(".bmp", Qt::CaseInsensitive)) {
        fileName.append(".bmp");
    }

    if (ui->tabWidget->count() > 0) {
        CustomTabForImage *currentTab = (CustomTabForImage *)ui->tabWidget->currentWidget();
        QImage *image = currentTab->getProcessedQImage();
        if (image)
            image->save(fileName, "BMP", 100);
        else {
            QErrorMessage *noImage = new QErrorMessage();
            noImage->showMessage("No image! Process image before saving.");
        }
    } else {
        QErrorMessage *noTabs = new QErrorMessage();
        noTabs->showMessage("Open some file before saving!");
    }
}

void MainWindow::initializeImageFileDialog(QFileDialog &dialog, QFileDialog::AcceptMode acceptMode)
{
    static bool firstDialog = true;

    if (firstDialog) {
        firstDialog = false;
        const QStringList picturesLocations = QStandardPaths::standardLocations(QStandardPaths::PicturesLocation);
        dialog.setDirectory(picturesLocations.isEmpty() ? QDir::currentPath() : picturesLocations.last());
    }

    QStringList mimeTypeFilters;
    const QByteArrayList supportedMimeTypes = acceptMode == QFileDialog::AcceptOpen
        ? QImageReader::supportedMimeTypes() : QImageWriter::supportedMimeTypes();
    foreach (const QByteArray &mimeTypeName, supportedMimeTypes)
        mimeTypeFilters.append(mimeTypeName);
    mimeTypeFilters.sort();
    dialog.setMimeTypeFilters(mimeTypeFilters);
    dialog.selectMimeTypeFilter("image/bmp");
    if (acceptMode == QFileDialog::AcceptSave)
        dialog.setDefaultSuffix("bmp");
}

QString MainWindow::selectNewFile()
{
    QFileDialog dialog(this, tr("Open File"));
    initializeImageFileDialog(dialog, QFileDialog::AcceptOpen);

    if (dialog.exec()) {
        return dialog.selectedFiles().first();
    } else {
        return NULL;
    }
}

void MainWindow::on_tabWidget_tabCloseRequested(int index)
{
    ui->tabWidget->removeTab(index);
}

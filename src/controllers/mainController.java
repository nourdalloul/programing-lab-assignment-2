
package controllers;

import java.io.*;
import java.nio.file.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.task; 
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.layout.BorderPane;

public class mainController {

    @FXML private BorderPane rootPane;
    @FXML private ListView<task> taskListView; 
    @FXML private TextField titleField, addedByField, userField, countUserField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private DatePicker datePicker;
    @FXML private Label totalLabel, resultLabel1, resultLabel2, resultLabel3, resultLabel4, resultLabel5, openCountLabel, closedCountLabel;
    private final String FILE_PATH = "src/data/details.csv";
    
    @FXML
    public void initialize() {
        statusCombo.getItems().addAll("open", "closed");
        loadTasksFromCSV();
    }

    // 1. إضافة مهمة جديدة مع التحقق من البيانات
    @FXML
    private void handleAddTask() {
        // التحقق من البيانات
        if (titleField.getText().isEmpty() || statusCombo.getValue() == null
                || addedByField.getText().isEmpty() || datePicker.getValue() == null) {
            showAlert("خطأ", "يرجى تعبئة كافة الحقول");
            return;
        }

        // توليد ID تلقائي بناءً على عدد العناصر في القائمة + 1
        int newId = taskListView.getItems().size() + 1;

        String dateStr = datePicker.getValue().toString();
        
        task newTask = new task(
                newId,
                titleField.getText(),
                statusCombo.getValue(),
                addedByField.getText(),
                dateStr
        );

        // إضافة المهمة للقائمةوالملف النصي  وتحديث العداد
        taskListView.getItems().add(newTask);
        updateTotal();
        saveTaskToCSV(newTask);
        handleClear();
    }

    private void saveTaskToCSV(task t) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(t.getId() + " , " + t.getTitle() + " , " + t.getStatus() + " , "
                    + t.getAddedBy() + " , " + t.getCreationDate());
        } catch (IOException e) {
            System.err.println("خطأ أثناء الكتابة في الملف: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleClear() {
        titleField.clear();
        addedByField.clear();
        statusCombo.setValue(null);
        datePicker.setValue(null);
    }

    // عرض المهام لمستخدم معين (مرتبة حسب العنوان) 
    @FXML
    private void handleTasksByUser() {
        String user = userField.getText();
        if (user.isEmpty()) {
            return;
        }

        String result = taskListView.getItems().stream()
                .filter(t -> t.getAddedBy().equalsIgnoreCase(user))
                .map(t -> t.getTitle()) 
                .collect(Collectors.joining(", "));

        resultLabel1.setText(result.isEmpty() ? "No tasks found" : result);
    }

    // عرض أول 4 مهام مضافة 
    @FXML
    private void handleEarliest() {
        String result = taskListView.getItems().stream()
                .limit(4)
                .map(task::getTitle)
                .collect(Collectors.joining(" | "));
        
        resultLabel2.setText(result.isEmpty() ? "القائمة فارغة" : result);
    }

    // المهام التي تبدأ بحرف 'a' وطول عنوانها 7 حروف 
    @FXML
    private void handleSpecial() {
        String result = taskListView.getItems().stream()
                .filter(t -> t.getTitle().toLowerCase().startsWith("a") && t.getTitle().length() == 7)
                .map(task::getTitle)
                .collect(Collectors.joining(", "));
        
        resultLabel3.setText(result.isEmpty() ? "لا يوجد مطابق" : result);
    }

    // أكثر من أضاف مهام
    @FXML
    private void handleMostActive() {
        if (taskListView.getItems().isEmpty()) {
            resultLabel4.setText("لا يوجد بيانات");
            return;
        }

        Map.Entry<String, Long> topUser = taskListView.getItems().stream()
                .collect(Collectors.groupingBy(task::getAddedBy, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (topUser != null) {
            resultLabel4.setText(topUser.getKey() + " (" + topUser.getValue() + ")");
        }
    }

    //عد المهام المفتوحة والمغلقة 
    @FXML
    private void handleOpenCount() {
        long open = taskListView.getItems().stream()
                .filter(t -> t.getStatus().equalsIgnoreCase("open")).count();
        long closed = taskListView.getItems().stream()
                .filter(t -> t.getStatus().equalsIgnoreCase("closed")).count();

        openCountLabel.setText("Open: " + open);
        closedCountLabel.setText("Closed: " + closed);
    }

    //  عدد المهام لمستخدم محدد بالاسم 
    @FXML
    private void handleCountByUser() {
        String user = countUserField.getText();
        long count = taskListView.getItems().stream()
                .filter(t -> t.getAddedBy().equalsIgnoreCase(user))
                .count();
        
        resultLabel5.setText(String.valueOf(count));
    }

    // وظائف إضافية للمنيو والمظهر 
    private void updateTotal() {
        totalLabel.setText(String.valueOf(taskListView.getItems().size()));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML private void handleExit() { System.exit(0); }
    
    @FXML private void handleAbout() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("About System");
        info.setContentText("Task Management System  is a JavaFX-based desktop application designed to help users manage and organize tasks efficiently. The system allows users to add, view, and analyze tasks using a structured and user-friendly graphical interface. ");
        info.showAndWait();
    }

      // تغيير نوع الخط لكل الواجهة
    @FXML
    private void changeFontFamily(javafx.event.ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String family = item.getText();
        rootPane.setStyle(rootPane.getStyle() + "-fx-font-family: '" + family + "';");
    }

    // تغيير حجم الخط لكل الواجهة
    @FXML
    private void changeFontSize(javafx.event.ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String size = item.getText().replace("px", "");
        rootPane.setStyle(rootPane.getStyle() + "-fx-font-size: " + size + "px;");
    }

    // تغيير ستايل الخط (Normal / Italic)
    @FXML
    private void changeFontStyle(javafx.event.ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        String style = item.getText().equalsIgnoreCase("Italic") ? "italic" : "normal";
        rootPane.setStyle(rootPane.getStyle() + "-fx-font-style: " + style + ";");
    }
     //تحميل المهام من الملف الى ال list  عند تشغيل البرنامج
    private void loadTasksFromCSV() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    task loadedTask = new task(
                            Integer.parseInt(data[0].trim()),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim()
                    );
                    taskListView.getItems().add(loadedTask);
                }
            }
            updateTotal();
        } catch (IOException e) {
            System.err.println("خطأ أثناء قراءة الملف: " + e.getMessage());
        }
    }
}
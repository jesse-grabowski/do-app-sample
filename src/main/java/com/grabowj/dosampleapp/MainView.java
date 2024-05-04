package com.grabowj.dosampleapp;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Route
public class MainView extends VerticalLayout {

    private final NoteRepository noteRepository;
    private final NoteEditor noteEditor;

    private final Grid<Note> noteGrid = new Grid<>(Note.class);
    private final Button addNewButton = new Button("New note", VaadinIcon.PLUS.create());

    public MainView(NoteRepository noteRepository, NoteEditor noteEditor) {
        this.noteRepository = noteRepository;
        this.noteEditor = noteEditor;
        
        add(new H1("CI/CD Test"), addNewButton, noteGrid, noteEditor);

        noteGrid.setHeight("300px");
        noteGrid.setColumns("id", "text");
        noteGrid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        noteGrid.asSingleSelect().addValueChangeListener(e -> {
            noteEditor.editNote(e.getValue());
        });

        addNewButton.addClickListener(e -> noteEditor.editNote(new Note()));

        noteEditor.setChangeHandler(() -> {
            noteEditor.setVisible(false);
            noteGrid.setItems(VaadinSpringDataHelpers.fromPagingRepository(noteRepository));
        });

        noteGrid.setItems(VaadinSpringDataHelpers.fromPagingRepository(noteRepository));
    }
}

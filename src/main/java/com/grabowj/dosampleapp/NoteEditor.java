package com.grabowj.dosampleapp;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class NoteEditor extends VerticalLayout implements KeyNotifier {

    private final NoteRepository noteRepository;

    private final TextArea text = new TextArea("Text");
    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private final HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private final Binder<Note> binder = new Binder<>(Note.class);

    private Note note;
    private ChangeHandler changeHandler;

    public NoteEditor(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;

        add(text, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editNote(note));
        setVisible(false);
    }

    void delete() {
        noteRepository.delete(note);
        changeHandler.onChange();
    }

    void save() {
        noteRepository.save(note);
        changeHandler.onChange();
    }

    public final void editNote(Note n) {
        if (n == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = n.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            // In a more complex app, you might want to load
            // the entity/DTO with lazy loaded relations for editing
            note = noteRepository.findById(n.getId()).get();
        }
        else {
            note = n;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(note);

        setVisible(true);

        // Focus first name initially
        text.focus();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

}

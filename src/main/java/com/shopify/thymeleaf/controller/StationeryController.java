package com.shopify.thymeleaf.controller;

import com.shopify.thymeleaf.model.Stationery;
import com.shopify.thymeleaf.repository.StationeryRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/stationery/")
public class StationeryController {

    private final StationeryRepository stationeryRepository;

    @Autowired
    public StationeryController(StationeryRepository stationeryRepository) {
        this.stationeryRepository = stationeryRepository;
    }

    @GetMapping("signup")
    public String showSignUpForm(Stationery stationery) {
        return "add-stationery";
    }

    @GetMapping("list")
    public String showUpdateForm(Model model) {
        model.addAttribute("stationery", stationeryRepository.findAll());
        return "index";
    }

    @PostMapping("add")
    public String addStationery(@Valid Stationery stationery, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-stationery";
        }

        stationeryRepository.save(stationery);
        return "redirect:list";
    }

    @GetMapping("edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Stationery stationery = stationeryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid stationery Id:" + id));
        model.addAttribute("stationery", stationery);
        return "update-stationery";
    }

    @PostMapping("update/{id}")
    public String updateStationery(@PathVariable("id") long id, @Valid Stationery stationery, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            stationery.setId(id);
            return "update-stationery";
        }

        stationeryRepository.save(stationery);
        model.addAttribute("stationery", stationeryRepository.findAll());
        return "index";
    }

    @GetMapping("delete/{id}")
    public String deleteStudent(@PathVariable("id") long id, Model model) {
        Stationery student = stationeryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid stationery Id:" + id));
        stationeryRepository.delete(student);
        model.addAttribute("stationery", stationeryRepository.findAll());
        return "index";
    }
    @GetMapping("/export-data")
    public void exportCSV(HttpServletResponse response)
            throws Exception {

        // set file name and content type
        String filename = "stationeryInventory.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        // create a csv writer
        StatefulBeanToCsv<Stationery> writer =
                new StatefulBeanToCsvBuilder
                        <Stationery>(response.getWriter())
                        .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).
                        withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                        .withOrderedResults(false).build();

        // write all employees to csv file
        writer.write(stationeryRepository.findAll());

    }

}
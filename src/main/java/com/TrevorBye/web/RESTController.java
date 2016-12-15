package com.TrevorBye.web;

import com.TrevorBye.POJO.DomNode;
import com.TrevorBye.POJO.ErrorResponse;
import com.TrevorBye.POJO.ValidUrl;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.TrevorBye.POJO.StaticMethods.*;

@RestController
public class RESTController {

    @RequestMapping("/getFullDomStructure/**")
    public ResponseEntity<?> getFullDomStructure(HttpServletRequest request) throws IOException {
        String encodedUrl = new AntPathMatcher().extractPathWithinPattern("/getFullDomStructure/**", request.getRequestURI());
        String scrapeUrl = URLDecoder.decode(URLDecoder.decode(encodedUrl,"UTF-8"), "UTF-8");
        List<Element> elementList;

        try {
            elementList = getAllElements(scrapeUrl);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("MALFORMED URL."), HttpStatus.BAD_REQUEST);
        }

        if (elementList.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("DOM STRUCTURE IS MISSING OR OTHERWISE NOT UNDERSTOOD BY THIS API."), HttpStatus.NO_CONTENT);
        }

        List<DomNode> domNodes = new ArrayList<>();
        for (Element element : elementList) {
            domNodes.add(buildDomReturn(element));
        }

        return new ResponseEntity<>(domNodes, HttpStatus.OK);
    }

    //returns full list of DomNodes (including children) by class starting with a specific String
    @RequestMapping("/getDomNodeListByClassStarting/{className}/**")
    public ResponseEntity<?> getDomNodeListByClassStarting(@PathVariable String className, HttpServletRequest request) throws IOException {
        String encodedUrl = new AntPathMatcher().extractPathWithinPattern("/getElementListByClassStarting/{className}/**", request.getRequestURI());
        String scrapeUrl = URLDecoder.decode(URLDecoder.decode(encodedUrl,"UTF-8"), "UTF-8");

        //only decoded once to deal with "+" escaped spaces.
        String decodedClassName = URLDecoder.decode(className, "UTF-8");
        List<Element> elementList;

        try {
            elementList = getClassListStarting(decodedClassName, scrapeUrl);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("MALFORMED URL."), HttpStatus.BAD_REQUEST);
        }

        if (elementList.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponse("CLASS COULD NOT BE FOUND."), HttpStatus.NO_CONTENT);
        }

        List<DomNode> domNodes = new ArrayList<>();
        for (Element element : elementList) {
            domNodes.add(buildDomReturn(element));
        }

        return new ResponseEntity<>(domNodes, HttpStatus.OK);
    }

    @RequestMapping("/getDomNodeById/{id:.+}/**")
    public ResponseEntity<?> getDomNodeById(@PathVariable String id, HttpServletRequest request) throws IOException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String encodedUrl = pathMatcher.extractPathWithinPattern("/getElementById/{id}/**", request.getRequestURI());
        String scrapeUrl = URLDecoder.decode(URLDecoder.decode(encodedUrl,"UTF-8"), "UTF-8");

        //only decoded once to handle "+" escaped spaces.
        String decodedId = URLDecoder.decode(id, "UTF-8");
        Element element;

        //catch error if URL is malformed
        try {
            element = getById(scrapeUrl,decodedId);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("MALFORMED URL."), HttpStatus.BAD_REQUEST);
        }

        if (element == null) {
            return new ResponseEntity<>(new ErrorResponse("ID COULD NOT BE FOUND."), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(buildDomReturn(element), HttpStatus.OK);
        }

    }

    @RequestMapping("/getAllPageLinks/{abs}/**")
    public ResponseEntity<?> hRefList(@PathVariable Boolean abs, HttpServletRequest request) throws IOException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String encodedUrl = pathMatcher.extractPathWithinPattern("/getAllPageLinks/{abs}/**", request.getRequestURI());
        String scrapeUrl = URLDecoder.decode(URLDecoder.decode(encodedUrl,"UTF-8"), "UTF-8");

        List<String> pageLinks;

        try {
            pageLinks = getAllPageLinks(scrapeUrl, abs);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("MALFORMED URL."), HttpStatus.BAD_REQUEST);
        }

        if (pageLinks.size() == 0) {
            return new ResponseEntity<>(new ErrorResponse("NO LINKS FOUND. POTENTIALLY CORRUPT DOM STRUCTURE."), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(pageLinks, HttpStatus.OK);
        }

    }

    //verifies whether or not requested URL will work with this API; returns either a 200 OK or a 400 bad request
    @RequestMapping("/validUrl/**")
    public ResponseEntity<?> isUrlValid(HttpServletRequest request) throws IOException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String encodedUrl = pathMatcher.extractPathWithinPattern("/validUrl/**", request.getRequestURI());
        String scrapeUrl = URLDecoder.decode(URLDecoder.decode(encodedUrl,"UTF-8"), "UTF-8");

        try {
            validJsoupUrl(scrapeUrl);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ValidUrl(false), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ValidUrl(true), HttpStatus.OK);
    }

}


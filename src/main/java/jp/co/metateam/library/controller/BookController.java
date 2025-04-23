package jp.co.metateam.library.controller;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.exec.spi.AbstractJdbcOperationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.AccountDto;
import jp.co.metateam.library.model.BookMst;
import jp.co.metateam.library.model.BookMstDto;
import jp.co.metateam.library.service.BookMstService;
import lombok.extern.log4j.Log4j2;

/**
 * 書籍関連クラス
 */
@Log4j2
@Controller
public class BookController {

    private final BookMstService bookMstService;

    @Autowired
    public BookController(BookMstService bookMstService) {
        this.bookMstService = bookMstService;
    }

    @GetMapping("/book/index")
    public String index(Model model) {
        // 書籍を全件取得
        List<BookMstDto> bookMstList = this.bookMstService.findAvailableWithStockCount();

        model.addAttribute("bookMstList", bookMstList);

        return "book/index";
    }

    @GetMapping("/book/add")
    public String add(Model model) {
        if (!model.containsAttribute("bookMstDto")) {
            model.addAttribute("bookMstDto", new BookMstDto());
            //bookmstdtoを複製　bookmstdtoは
        }

        return "book/add";
    }

    @PostMapping("/book/add")
    public String add(@Valid @ModelAttribute BookMstDto bookMstDto, BindingResult result, RedirectAttributes ra,Model model) {

        String title = bookMstDto.getTitle();
        String isbn = bookMstDto.getIsbn();

        List<String>errortitlelist = new ArrayList<String>();
        List<String>errorisbnlist = new ArrayList<String>();

        //書籍名必須チェック
        if(StringUtils.isEmpty(title)){
            //エラーメッセージ
            errortitlelist.add("書籍名は必須です");
        
        }
    

        //書籍名桁数チェック
        if(title.length() > 255){
        
          errortitlelist.add("書籍名は255文字以下で入力してください");
          
        }

// ISBN必須チェック
      if(StringUtils.isEmpty(isbn)){
       errorisbnlist.add("ISBNを入力してください");
       
     }else{
// ISBN桁数チェック
    if(isbn.length() != 13){
       errorisbnlist.add("ISBNは13文字で入力してください");
       
     }
// ISBN文字種チェック
     if (!isbn.matches("^[0-9]+$")) {
       errorisbnlist.add("ISBNの形式が不明です");
    

    } 

    //ISBN重複チェック
        if (isbn != null && !isbn.isEmpty()) {
            long count = bookMstService.countByIsbn(isbn);
            if (count > 0) {
        errorisbnlist.add("登録済みのISBNです");
       }
    }
    }
      
boolean hasError = false;
if (!errortitlelist.isEmpty()){
    model.addAttribute("errortitle", errortitlelist);
    hasError =  true;
}
        if (!errorisbnlist.isEmpty()){
            model.addAttribute("errorIsbn", errorisbnlist);
            hasError = true;
        }
        if(hasError){
        return "book/add";
    
    }

     

 this.bookMstService.save(bookMstDto);
     return "redirect:/book/index";

    }
}


